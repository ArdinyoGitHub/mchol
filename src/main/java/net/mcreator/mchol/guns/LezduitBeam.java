package net.mcreator.mchol.guns;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;

import net.mcreator.mchol.bounty.BountyEntities;

import java.util.Comparator;
import java.util.List;

/**
 * Lezduit, the broken talking gun. While the trigger is held he fires a lightning storm for up to 5 seconds (the "magazine",
 * see GunSpec): every shot throws a jagged pink arc at EVERY mob on the player's screen at once, sometimes with a (harmless,
 * fireless) lightning bolt from the sky on top. When the charge is used up the long recharge (GunSpec reload) starts.
 */
public class LezduitBeam {
	/** How far the storm reaches, in blocks. */
	private static final double RANGE = 40;
	/**
	 * Cosine of the largest angle between the look direction and a mob that still counts as "on screen".
	 * 0.55 ≈ 57°, which covers the corners of a 16:9 screen at the default 70° FOV.
	 */
	private static final double VIEW_COS = 0.55;
	/** Most mobs hit by one shot (keeps the particle count sane). */
	private static final int MAX_TARGETS = 20;
	/** Chance per shot and target that a lightning bolt also comes down from the sky. */
	private static final float BOLT_CHANCE = 0.3f;
	/** Ticks between two thunder sounds while firing. */
	private static final int THUNDER_INTERVAL = 16;

	/** True while the storm is dealing damage (server thread only), so GunHandler shortens the hurt cooldown. */
	static boolean HURTING;

	public static void fire(ServerPlayer player, GunSpec spec, Vec3 look, long now) {
		if (!(player.level() instanceof ServerLevel level))
			return;
		Vec3 eye = player.getEyePosition();
		Vec3 muzzle = muzzle(player, look);
		List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(RANGE), e -> isTarget(e, player) && onScreen(e, eye, look));
		targets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));

		if (targets.isEmpty()) {
			// nothing to hit: the arc goes where the player is looking
			HitResult hit = level.clip(new ClipContext(eye, eye.add(look.scale(RANGE)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
			Vec3 to = hit.getLocation();
			drawArc(level, spec.muzzleColor(), muzzle, to);
			if (hit.getType() != HitResult.Type.MISS)
				level.sendParticles(ParticleTypes.ELECTRIC_SPARK, true, false, to.x, to.y, to.z, 6, 0.2, 0.2, 0.2, 0.15);
		} else {
			for (int i = 0; i < targets.size() && i < MAX_TARGETS; i++) {
				LivingEntity target = targets.get(i);
				Vec3 to = target.getBoundingBox().getCenter();
				drawArc(level, spec.muzzleColor(), muzzle, to);
				if (level.getRandom().nextFloat() < BOLT_CHANCE)
					bolt(level, target.position());
				level.sendParticles(ParticleTypes.ELECTRIC_SPARK, true, false, to.x, to.y, to.z, 6, 0.3, 0.4, 0.3, 0.2);
				hurt(level, player, target, spec.damage());
			}
		}

		var sound = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(spec.sound()));
		if (sound != null)
			level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 0.5f, 1.6f + level.getRandom().nextFloat() * 0.3f);
		if (now % THUNDER_INTERVAL < spec.fireInterval())
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.5f, 1.4f + level.getRandom().nextFloat() * 0.2f);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.5f, 1.6f);
	}

	private static void bolt(ServerLevel level, Vec3 at) {
		LightningBolt bolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
		if (bolt == null)
			return;
		bolt.setPos(at.x, at.y, at.z);
		bolt.setVisualOnly(true); // no fire, no charged creepers, no extra damage
		bolt.setSilent(true); // the gun plays its own thunder
		level.addFreshEntity(bolt);
	}

	private static void hurt(ServerLevel level, ServerPlayer player, LivingEntity target, double damage) {
		HURTING = true;
		try {
			target.hurtServer(level, level.damageSources().indirectMagic(player, player), (float) damage);
		} finally {
			HURTING = false;
		}
		if (!target.isAlive())
			LezduitVoice.onKill(player);
	}

	/** Every mob, except villagers, the shopkeepers and the player's own pets. Never players. */
	private static boolean isTarget(LivingEntity e, Player player) {
		if (!(e instanceof Mob) || !e.isAlive() || e.isSpectator() || e.isInvulnerable())
			return false;
		if (e instanceof AbstractVillager || BountyEntities.of(e) instanceof BountyEntities kind && kind.isShopkeeper())
			return false;
		return !(e instanceof OwnableEntity pet && pet.getOwner() == player);
	}

	/** In front of the player, inside the view cone and not behind a wall. */
	private static boolean onScreen(LivingEntity e, Vec3 eye, Vec3 look) {
		Vec3 to = e.getBoundingBox().getCenter().subtract(eye);
		double dist = to.length();
		if (dist > RANGE || dist < 1.0E-3)
			return dist < 1.0E-3;
		if (to.scale(1 / dist).dot(look) < VIEW_COS)
			return false;
		// line of sight from the eyes to the mob's centre or head
		return e.level().clip(new ClipContext(eye, e.getBoundingBox().getCenter(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e)).getType() == HitResult.Type.MISS
				|| e.level().clip(new ClipContext(eye, e.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e)).getType() == HitResult.Type.MISS;
	}

	private static Vec3 muzzle(ServerPlayer player, Vec3 look) {
		Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize().scale(player.getMainArm() == HumanoidArm.RIGHT ? 0.35 : -0.35);
		return player.getEyePosition().add(look.scale(1.0)).add(right).add(0, -0.25, 0);
	}

	/** A zigzag lightning arc: the line is split into segments whose joints are pushed sideways at random. */
	private static void drawArc(ServerLevel level, int color, Vec3 from, Vec3 to) {
		Vec3 dir = to.subtract(from);
		double length = dir.length();
		if (length < 0.01)
			return;
		Vec3 side = dir.cross(new Vec3(0, 1, 0));
		if (side.lengthSqr() < 1.0E-4)
			side = new Vec3(1, 0, 0);
		side = side.normalize();
		Vec3 up = side.cross(dir).normalize();
		int segments = Math.max(3, (int) (length / 1.5));
		double jitter = Math.min(0.8, 0.15 + length * 0.04);
		DustParticleOptions core = new DustParticleOptions(color, 1.0f);
		DustParticleOptions glow = new DustParticleOptions(0xFFFFFF, 0.5f);
		Vec3 prev = from;
		for (int i = 1; i <= segments; i++) {
			Vec3 next = from.add(dir.scale(i / (double) segments));
			if (i < segments)
				next = next.add(side.scale((level.getRandom().nextDouble() - 0.5) * 2 * jitter)).add(up.scale((level.getRandom().nextDouble() - 0.5) * 2 * jitter));
			Vec3 step = next.subtract(prev);
			int points = Math.max(2, (int) (step.length() / 0.3));
			for (int p = 0; p < points; p++) {
				Vec3 at = prev.add(step.scale(p / (double) points));
				level.sendParticles(core, true, false, at.x, at.y, at.z, 1, 0, 0, 0, 0);
				if (p % 3 == 0)
					level.sendParticles(glow, true, false, at.x, at.y, at.z, 1, 0, 0, 0, 0);
			}
			prev = next;
		}
	}

	/** Lezduit in someone's hand, no matter if he was registered yet (MCreator adds the item on "Regenerate code"). */
	public static boolean isLezduit(Entity holder) {
		return holder instanceof LivingEntity living && BuiltInRegistries.ITEM.getKey(living.getMainHandItem().getItem()).equals(Identifier.fromNamespaceAndPath("mchol", "lezduit"));
	}
}
