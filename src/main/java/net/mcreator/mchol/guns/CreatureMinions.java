package net.mcreator.mchol.guns;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;

import net.mcreator.mchol.entity.CreatureChildrenEntity;
import net.mcreator.mchol.McholMod;

import javax.annotation.Nullable;

import java.util.WeakHashMap;
import java.util.Map;

/**
 * Creature's primary fire, as in High on Life: every shot is one of Creature's children. Where it lands it becomes a
 * Creature Child (MCreator livingentity "CreatureMinion") that runs to the nearest enemy, latches on and keeps biting
 * until it pops. Kills and XP go to whoever fired the gun. The alt-fire (Possession) is left to AltFire.
 */
@EventBusSubscriber
public class CreatureMinions {
	private static final Identifier MINION = Identifier.fromNamespaceAndPath(McholMod.MODID, "creature_minion");
	/** A child pops after this many ticks no matter what. */
	private static final int LIFETIME = 140;
	/** ...or after biting for this long. */
	private static final int MAX_LATCH = 60;
	private static final int BITE_INTERVAL = 15;
	private static final float BITE_DAMAGE = 1.5f;
	/** Children alive at once per shooter; the oldest pops when a new one lands. */
	private static final int MAX_PER_OWNER = 6;
	private static final double SEARCH_RADIUS = 14;
	private static final int CYAN = 0x2FB8F0;

	private static final class State {
		@Nullable
		LivingEntity owner;
		@Nullable
		LivingEntity target;
		double damageScale = 1;
		int age;
		boolean latched;
		int latchedTicks;
		/** Where on the target the child holds on: angle relative to the target's body, distance from its axis, height. */
		float latchAngle;
		double latchRadius, latchHeight;
	}

	// server thread only
	private static final Map<Mob, State> STATES = new WeakHashMap<>();

	@Nullable
	private static EntityType<?> minionType() {
		return BuiltInRegistries.ENTITY_TYPE.getOptional(MINION).orElse(null);
	}

	public static boolean isMinion(Entity entity) {
		return entity.getType() == minionType();
	}

	private static boolean isChildShot(Entity entity) {
		return entity instanceof CreatureChildrenEntity && !AltFire.isAlt(entity);
	}

	@SubscribeEvent
	public static void onImpact(ProjectileImpactEvent event) {
		Projectile projectile = event.getProjectile();
		if (!isChildShot(projectile) || !(projectile.level() instanceof ServerLevel level) || !(projectile.getOwner() instanceof LivingEntity owner))
			return;
		HitResult hit = event.getRayTraceResult();
		LivingEntity hitEntity = hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living ? living : null;
		if (hitEntity != null && (hitEntity == owner || isMinion(hitEntity)))
			return; // let it fly on
		event.setCanceled(true);
		projectile.discard();
		Vec3 pos = hit.getLocation();
		if (hitEntity == null) // step back out of the block it hit
			pos = pos.subtract(projectile.getDeltaMovement().normalize().scale(0.3));
		spawn(level, pos, owner, hitEntity);
	}

	/** Called by GunHandler when a shot runs out of flight time: the child drops out of the air there. */
	static void onProjectileExpired(Entity projectile) {
		if (isChildShot(projectile) && projectile.level() instanceof ServerLevel level && projectile instanceof Projectile p && p.getOwner() instanceof LivingEntity owner)
			spawn(level, projectile.position(), owner, null);
	}

	private static void spawn(ServerLevel level, Vec3 pos, LivingEntity owner, @Nullable LivingEntity hitEntity) {
		EntityType<?> type = minionType();
		if (type == null || !(type.create(level, EntitySpawnReason.MOB_SUMMONED) instanceof Mob minion))
			return;
		popOldestIfFull(owner);
		minion.snapTo(pos.x, pos.y, pos.z, owner.getYRot(), 0);
		State state = new State();
		state.owner = owner;
		state.damageScale = owner instanceof Player ? 1.0 : 0.75;
		STATES.put(minion, state);
		level.addFreshEntity(minion);
		if (hitEntity != null && isValidTarget(state, minion, hitEntity))
			latch(minion, state, hitEntity);
		level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.SILVERFISH_AMBIENT, SoundSource.PLAYERS, 0.6f, 1.6f);
	}

	private static void popOldestIfFull(LivingEntity owner) {
		Mob oldest = null;
		int count = 0;
		for (Map.Entry<Mob, State> entry : STATES.entrySet()) {
			if (entry.getValue().owner != owner || !entry.getKey().isAlive())
				continue;
			count++;
			if (oldest == null || entry.getValue().age > STATES.get(oldest).age)
				oldest = entry.getKey();
		}
		if (count >= MAX_PER_OWNER && oldest != null)
			pop(oldest);
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof Mob minion) || !(minion.level() instanceof ServerLevel level) || !isMinion(minion))
			return;
		// children from a spawn egg or reloaded from disk have no shooter: they act on their own
		State state = STATES.computeIfAbsent(minion, m -> new State());
		if (++state.age > LIFETIME) {
			pop(minion);
			return;
		}
		if (state.target != null && !isValidTarget(state, minion, state.target)) {
			state.target = null;
			if (state.latched)
				unlatch(minion, state);
		}
		if (state.latched) {
			tickLatched(level, minion, state);
			return;
		}
		if (state.target == null && minion.tickCount % 5 == 0)
			state.target = findTarget(level, minion, state);
		LivingEntity target = state.target;
		if (target == null) {
			// nothing to bite: stay near the shooter
			if (state.owner != null && minion.distanceToSqr(state.owner) > 36)
				minion.getNavigation().moveTo(state.owner, 1.4);
			return;
		}
		minion.getLookControl().setLookAt(target);
		if (minion.getBoundingBox().inflate(0.4).intersects(target.getBoundingBox())) {
			latch(minion, state, target);
			return;
		}
		minion.getNavigation().moveTo(target, 1.6);
		double distance = minion.distanceTo(target);
		if (distance < 4 && minion.onGround() && minion.getRandom().nextInt(6) == 0) {
			// pounce
			Vec3 leap = target.position().subtract(minion.position()).multiply(1, 0, 1).normalize().scale(0.55);
			minion.setDeltaMovement(leap.x, 0.38, leap.z);
			minion.needsSync = true;
		}
	}

	private static void tickLatched(ServerLevel level, Mob minion, State state) {
		LivingEntity target = state.target;
		if (target == null || ++state.latchedTicks > MAX_LATCH) {
			pop(minion);
			return;
		}
		float angle = (target.yBodyRot + state.latchAngle) * Mth.DEG_TO_RAD;
		double x = target.getX() - Mth.sin(angle) * state.latchRadius;
		double z = target.getZ() + Mth.cos(angle) * state.latchRadius;
		minion.setPos(x, target.getY() + state.latchHeight, z);
		minion.setDeltaMovement(Vec3.ZERO);
		minion.fallDistance = 0;
		// face the target
		float yaw = (float) (Mth.atan2(target.getZ() - z, target.getX() - x) * Mth.RAD_TO_DEG) - 90;
		minion.setYRot(yaw);
		minion.setYHeadRot(yaw);
		minion.yBodyRot = yaw;
		if (state.latchedTicks % BITE_INTERVAL == 0)
			bite(level, minion, state, target);
	}

	private static void bite(ServerLevel level, Mob minion, State state, LivingEntity target) {
		DamageSource source = state.owner != null ? level.damageSources().thrown(minion, state.owner) : level.damageSources().mobAttack(minion);
		// several children on one mob all get their bite in
		if (!(target instanceof Player))
			target.setInvulnerableTime(0);
		target.hurtServer(level, source, (float) (BITE_DAMAGE * state.damageScale));
		level.playSound(null, minion.getX(), minion.getY(), minion.getZ(), SoundEvents.FOX_BITE, SoundSource.PLAYERS, 0.5f, 1.4f + minion.getRandom().nextFloat() * 0.3f);
		level.sendParticles(ParticleTypes.CRIT, minion.getX(), minion.getY() + 0.3, minion.getZ(), 3, 0.15, 0.15, 0.15, 0.1);
	}

	private static void latch(Mob minion, State state, LivingEntity target) {
		state.target = target;
		state.latched = true;
		state.latchedTicks = 0;
		state.latchAngle = minion.getRandom().nextFloat() * 360;
		state.latchRadius = target.getBbWidth() / 2 + 0.05;
		state.latchHeight = target.getBbHeight() * (0.3 + minion.getRandom().nextFloat() * 0.4);
		minion.getNavigation().stop();
		minion.setNoGravity(true);
	}

	private static void unlatch(Mob minion, State state) {
		state.latched = false;
		minion.setNoGravity(false);
	}

	private static void pop(Mob minion) {
		STATES.remove(minion);
		if (minion.level() instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.POOF, minion.getX(), minion.getY() + 0.3, minion.getZ(), 6, 0.15, 0.15, 0.15, 0.02);
			level.sendParticles(new DustParticleOptions(CYAN, 1.0f), minion.getX(), minion.getY() + 0.3, minion.getZ(), 8, 0.2, 0.2, 0.2, 0);
			level.playSound(null, minion.getX(), minion.getY(), minion.getZ(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 0.6f, 1.5f);
		}
		minion.discard();
	}

	@Nullable
	private static LivingEntity findTarget(ServerLevel level, Mob minion, State state) {
		// a mob's children go for whatever their shooter is fighting
		if (state.owner instanceof Mob mobOwner)
			return mobOwner.getTarget() != null && isValidTarget(state, minion, mobOwner.getTarget()) ? mobOwner.getTarget() : null;
		// a player's children prefer what the player just hit
		if (state.owner != null && state.owner.getLastHurtMob() instanceof LivingEntity last && last.distanceToSqr(minion) < SEARCH_RADIUS * SEARCH_RADIUS && isValidTarget(state, minion, last))
			return last;
		LivingEntity best = null;
		double bestDistance = Double.MAX_VALUE;
		for (LivingEntity candidate : level.getEntitiesOfClass(LivingEntity.class, minion.getBoundingBox().inflate(SEARCH_RADIUS), e -> isValidTarget(state, minion, e))) {
			double distance = candidate.distanceToSqr(minion);
			if (distance < bestDistance && minion.hasLineOfSight(candidate)) {
				bestDistance = distance;
				best = candidate;
			}
		}
		return best;
	}

	private static boolean isValidTarget(State state, Mob minion, LivingEntity target) {
		if (!target.isAlive() || target == minion || target == state.owner || target.level() != minion.level() || isMinion(target) || target.isInvulnerable())
			return false;
		if (target instanceof Player player && (player.isCreative() || player.isSpectator()))
			return false;
		if (target.distanceToSqr(minion) > (SEARCH_RADIUS * 2) * (SEARCH_RADIUS * 2))
			return false;
		LivingEntity owner = state.owner;
		if (owner instanceof Mob mobOwner)
			return target == mobOwner.getTarget() || target instanceof Player;
		if (target instanceof Player)
			return false;
		if (target instanceof TamableAnimal pet && owner != null && pet.isOwnedBy(owner))
			return false;
		if (AltFire.isPossessed(target))
			return false;
		// hostile mobs, anything fighting the shooter, or whatever the shooter attacked
		return target instanceof Enemy || (owner != null && (target instanceof Mob mob && mob.getTarget() == owner || owner.getLastHurtMob() == target));
	}

	/** The shooter's own shots must not kill its children. */
	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		if (!(event.getEntity() instanceof Mob minion) || !isMinion(minion))
			return;
		State state = STATES.get(minion);
		if (state != null && state.owner != null && event.getSource().getEntity() == state.owner)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		STATES.clear();
	}
}
