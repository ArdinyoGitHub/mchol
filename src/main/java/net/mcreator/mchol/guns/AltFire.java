package net.mcreator.mchol.guns;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.DustParticleOptions;

import net.mcreator.mchol.init.McholModItems;
import net.mcreator.mchol.McholMod;

import javax.annotation.Nullable;

import java.util.WeakHashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Right click abilities, modelled after High on Life:
 * Kenny launches enemies into the air (the yellow Multi-Glob Kenny with several smaller globs), Gus pulls them into a vortex
 * (Goop Suck Gus instead strips their armor and gives it to you as absorption), Sweezy freezes them in a time bubble
 * (Portal Sweezy instead opens a portal that fires fully charged crystals) and Creature makes an enemy fight for you.
 *
 * @param count    projectiles per use, fanned out horizontally
 * @param cooldown ticks before the ability can be used again
 * @param lifetime ticks of flight; the area abilities and the portal deploy where the projectile is when it runs out
 * @param radius   area of the effect in blocks
 * @param duration how long the effect lasts in ticks (0 = instant)
 */
@EventBusSubscriber
public enum AltFire {
	GLOB("Glob", McholModItems.KENNY, 1, 100, 1.4f, true, 40, 3.5, 0, 0x9BE23C),
	MULTI_GLOB("Multi-Glob", McholModItems.MULTI_GLOB_KENNY, 3, 140, 1.4f, true, 40, 2.5, 0, 0xFFE53B),
	GOOP_SUCK("Vortex", McholModItems.GUS, 1, 160, 1.8f, false, 15, 7, 60, 0x7FD13B),
	// Goopsuck mod: no pull, the goop armor of every enemy in the cloud is sucked off and becomes your absorption
	GOOP_STRIP("Goop Suck", McholModItems.GOOP_SUCK_GUS, 1, 200, 1.8f, false, 15, 6, 60, 0x4FE08A),
	TIME_BUBBLE("Time Bubble", McholModItems.SWEEZY, 1, 240, 2.0f, false, 20, 4.5, 100, 0x6FD5FF),
	// Summoning Portal mod: radius = how far the portal looks for a target, duration = how long it stays open
	// (51 = PORTAL_DELAY + PORTAL_INTERVAL * (PORTAL_SHOTS - 1) + 1, so it fires exactly three crystals)
	PORTAL("Summoning Portal", McholModItems.PORTAL_SWEEZY, 1, 220, 2.0f, false, 20, 16, 51, 0xB05CFF),
	POSSESSION("Possession", McholModItems.CREATUREE, 1, 300, 2.2f, true, 40, 20, 200, 0xFF9A2E);

	/** Angle between the globs of a multi-projectile alt-fire, in degrees. */
	private static final float FAN_ANGLE = 12;

	private static final String ALT_KEY = "mchol_alt";

	/** Summoning Portal: ticks before the first charged crystal, ticks between crystals, crystals per portal and their damage. */
	private static final int PORTAL_DELAY = 20, PORTAL_INTERVAL = 15, PORTAL_SHOTS = 3;
	private static final double PORTAL_DAMAGE = 10;
	/** Goop Suck: how long a stripped enemy stays without armor and how long the stolen absorption lasts. */
	private static final int STRIP_TICKS = 200, ABSORPTION_TICKS = 600, MAX_ABSORPTION_LEVEL = 4;
	private static final Identifier STRIPPED_ARMOR = Identifier.fromNamespaceAndPath(McholMod.MODID, "goop_stripped");

	public final String displayName;
	private final DeferredItem<Item> gun;
	private final int count;
	public final int cooldown;
	public final float speed;
	private final boolean gravity;
	public final int lifetime;
	private final double radius;
	private final int duration;
	public final int color;

	AltFire(String displayName, DeferredItem<Item> gun, int count, int cooldown, float speed, boolean gravity, int lifetime, double radius, int duration, int color) {
		this.displayName = displayName;
		this.gun = gun;
		this.count = count;
		this.cooldown = cooldown;
		this.speed = speed;
		this.gravity = gravity;
		this.lifetime = lifetime;
		this.radius = radius;
		this.duration = duration;
		this.color = color;
	}

	@Nullable
	public static AltFire get(ItemStack stack) {
		for (AltFire alt : values()) {
			if (!stack.isEmpty() && stack.is(alt.gun.get()))
				return alt;
		}
		return null;
	}

	/** Active Goop Suck vortexes and Time Bubbles. */
	private record Field(AltFire type, ServerLevel level, Vec3 center, @Nullable Entity owner, long start, long end, Vec3 aim) {
	}

	/** Enemies whose armor Goop Suck took -> game time when it comes back. */
	private static final Map<LivingEntity, Long> STRIPPED = new WeakHashMap<>();

	private static final List<Field> FIELDS = new ArrayList<>();
	/** Possessed mob -> game time when the possession ends. */
	private static final Map<Mob, Long> POSSESSED = new WeakHashMap<>();

	public void fire(ServerPlayer player, GunSpec spec) {
		fire(player, spec, player.getLookAngle());
	}

	/** Also used by the bounty bosses (see bounty/BossFights), so the shooter can be any living entity. */
	public void fire(LivingEntity shooter, GunSpec spec, Vec3 direction) {
		Vec3 look = direction.normalize();
		for (int i = 0; i < count; i++) {
			float yaw = (i - (count - 1) / 2f) * FAN_ANGLE;
			Projectile projectile = GunHandler.spawnProjectile(shooter, spec, look.yRot(yaw * Mth.DEG_TO_RAD), speed, 0, 2, lifetime, gravity);
			projectile.getPersistentData().putString(ALT_KEY, name());
		}
		SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(spec.sound()));
		if (sound != null)
			shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), sound, shooter instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1.2f, 0.6f);
	}

	@Nullable
	private static AltFire of(Entity projectile) {
		String name = projectile.getPersistentData().getStringOr(ALT_KEY, "");
		for (AltFire alt : values()) {
			if (alt.name().equals(name))
				return alt;
		}
		return null;
	}

	/** Whether the projectile is an alt-fire shot (handled here) rather than a normal one. */
	public static boolean isAlt(Entity projectile) {
		return of(projectile) != null;
	}

	/** Whether the mob is currently possessed by Creature, i.e. fighting on the player's side. */
	public static boolean isPossessed(Entity entity) {
		return entity instanceof Mob mob && POSSESSED.containsKey(mob);
	}

	/** Called by GunHandler when an alt projectile runs out of flight time without hitting anything. */
	static void onExpired(Entity projectile) {
		AltFire alt = of(projectile);
		if (alt != null && projectile.level() instanceof ServerLevel level && alt != POSSESSION)
			alt.trigger(level, projectile, projectile.position(), null);
	}

	/** Called every tick for alt projectiles so they can be told apart from normal shots. */
	static void trail(Entity projectile) {
		AltFire alt = of(projectile);
		if (alt != null && projectile.level() instanceof ServerLevel level)
			level.sendParticles(new DustParticleOptions(alt.color, 1.5f), projectile.getX(), projectile.getY(), projectile.getZ(), 2, 0.05, 0.05, 0.05, 0);
	}

	@SubscribeEvent
	public static void onImpact(ProjectileImpactEvent event) {
		Projectile projectile = event.getProjectile();
		if (!(projectile.level() instanceof ServerLevel level))
			return;
		AltFire alt = of(projectile);
		if (alt == null)
			return;
		HitResult hit = event.getRayTraceResult();
		Entity hitEntity = hit instanceof EntityHitResult entityHit ? entityHit.getEntity() : null;
		alt.trigger(level, projectile, hit.getLocation(), hitEntity);
		event.setCanceled(true);
		projectile.discard();
	}

	private void trigger(ServerLevel level, Entity projectile, Vec3 pos, @Nullable Entity hitEntity) {
		Entity owner = projectile instanceof Projectile p ? p.getOwner() : null;
		long now = level.getGameTime();
		switch (this) {
			case GLOB, MULTI_GLOB -> {
				for (LivingEntity target : targetsAround(level, pos, radius, owner)) {
					target.hurtServer(level, level.damageSources().thrown(projectile, owner), 4);
					target.setDeltaMovement(target.getDeltaMovement().x * 0.3, 1.1, target.getDeltaMovement().z * 0.3);
					target.needsSync = true;
					target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 0));
				}
				level.sendParticles(ParticleTypes.ITEM_SLIME, pos.x, pos.y, pos.z, 40, radius / 3, 0.5, radius / 3, 0.2);
				level.sendParticles(new DustParticleOptions(color, 2f), pos.x, pos.y, pos.z, 25, radius / 3, 0.5, radius / 3, 0);
				level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 1.5f, 0.6f);
			}
			case GOOP_SUCK, TIME_BUBBLE, GOOP_STRIP -> {
				FIELDS.add(new Field(this, level, pos, owner, now, now + duration, Vec3.ZERO));
				level.playSound(null, pos.x, pos.y, pos.z, this == TIME_BUBBLE ? SoundEvents.BEACON_ACTIVATE : SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 0.6f, this == GOOP_STRIP ? 0.8f : 1.5f);
			}
			case PORTAL -> {
				Vec3 aim = projectile.getDeltaMovement().lengthSqr() > 1.0E-6 ? projectile.getDeltaMovement().normalize() : (owner != null ? owner.getLookAngle() : new Vec3(0, 0, 1));
				// open a little in front of whatever was hit, at chest height
				Vec3 center = pos.subtract(aim.scale(1.0)).add(0, 1.0, 0);
				FIELDS.add(new Field(this, level, center, owner, now, now + duration, aim));
				level.playSound(null, center.x, center.y, center.z, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.5f, 0.6f);
			}
			case POSSESSION -> {
				if (hitEntity instanceof Mob mob && hitEntity != owner) {
					POSSESSED.put(mob, now + duration);
					mob.setTarget(null);
					mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0, false, false));
					level.sendParticles(ParticleTypes.ANGRY_VILLAGER, mob.getX(), mob.getEyeY() + 0.5, mob.getZ(), 6, 0.4, 0.3, 0.4, 0);
					level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1, 1.2f);
				}
				level.sendParticles(new DustParticleOptions(color, 1.5f), pos.x, pos.y, pos.z, 15, 0.3, 0.3, 0.3, 0);
			}
		}
	}

	/** Living entities in range, except the shooter and creative/spectator players. */
	private static List<LivingEntity> targetsAround(ServerLevel level, Vec3 center, double radius, @Nullable Entity owner) {
		return level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(radius),
				e -> e != owner && e.isAlive() && !e.isSpectator() && !(e instanceof Player p && p.isCreative()) && e.position().distanceToSqr(center) <= radius * radius);
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event) {
		Iterator<Field> fields = FIELDS.iterator();
		while (fields.hasNext()) {
			Field field = fields.next();
			long now = field.level.getGameTime();
			if (now >= field.end) {
				if (field.type == TIME_BUBBLE)
					field.level.playSound(null, field.center.x, field.center.y, field.center.z, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.6f, 1.5f);
				fields.remove();
				continue;
			}
			switch (field.type) {
				case GOOP_SUCK -> tickGoopSuck(field, now);
				case GOOP_STRIP -> tickGoopStrip(field, now);
				case PORTAL -> tickPortal(field, now);
				default -> tickTimeBubble(field, now);
			}
		}

		Iterator<Map.Entry<LivingEntity, Long>> stripped = STRIPPED.entrySet().iterator();
		while (stripped.hasNext()) {
			Map.Entry<LivingEntity, Long> entry = stripped.next();
			LivingEntity entity = entry.getKey();
			if (!entity.isAlive() || entity.level().getGameTime() >= entry.getValue()) {
				var armor = entity.getAttribute(Attributes.ARMOR);
				if (armor != null)
					armor.removeModifier(STRIPPED_ARMOR);
				stripped.remove();
			}
		}

		Iterator<Map.Entry<Mob, Long>> possessed = POSSESSED.entrySet().iterator();
		while (possessed.hasNext()) {
			Map.Entry<Mob, Long> entry = possessed.next();
			Mob mob = entry.getKey();
			if (!mob.isAlive() || !(mob.level() instanceof ServerLevel level) || level.getGameTime() >= entry.getValue()) {
				possessed.remove();
				mob.setTarget(null);
				continue;
			}
			if (mob.tickCount % 10 == 0) {
				LivingEntity target = mob.getTarget();
				if (target == null || !target.isAlive())
					mob.setTarget(nearestEnemy(level, mob));
			}
		}
	}

	private static void tickGoopSuck(Field field, long now) {
		Vec3 c = field.center;
		for (LivingEntity target : targetsAround(field.level, c, GOOP_SUCK.radius, field.owner)) {
			Vec3 toCenter = c.subtract(target.position().add(0, target.getBbHeight() / 2, 0));
			double distance = toCenter.length();
			if (distance < 0.3)
				continue;
			Vec3 pull = toCenter.normalize().scale(Math.min(0.4, 0.1 + distance * 0.04));
			target.setDeltaMovement(target.getDeltaMovement().scale(0.6).add(pull));
			target.needsSync = true;
			target.resetFallDistance();
		}
		// swirling ring
		for (int i = 0; i < 6; i++) {
			double angle = now * 0.6 + i * Math.PI / 3;
			double r = 1.0 + (now % 10) * 0.2;
			field.level.sendParticles(new DustParticleOptions(GOOP_SUCK.color, 1.2f), c.x + Math.cos(angle) * r, c.y + Math.sin(now * 0.3 + i) * 0.4, c.z + Math.sin(angle) * r, 1, 0, 0, 0, 0);
		}
		field.level.sendParticles(ParticleTypes.PORTAL, c.x, c.y, c.z, 4, 0.5, 0.5, 0.5, 0.5);
	}

	private static void tickTimeBubble(Field field, long now) {
		Vec3 c = field.center;
		double r = TIME_BUBBLE.radius;
		for (Entity entity : field.level.getEntities((Entity) null, new AABB(c, c).inflate(r), e -> e != field.owner && e.position().distanceToSqr(c) <= r * r)) {
			if (entity instanceof LivingEntity living) {
				if (living instanceof Player p && (p.isCreative() || p.isSpectator()))
					continue;
				living.setDeltaMovement(living.getDeltaMovement().scale(0.05));
				living.needsSync = true;
				living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 5, 6, false, false));
				if (living instanceof Mob mob)
					mob.getNavigation().stop();
			} else if (entity instanceof Projectile && !entity.entityTags().contains(GunHandler.PROJECTILE_TAG)) {
				// enemy projectiles freeze, your own shots fly through
				entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
			}
		}
		// bubble surface
		var random = field.level.getRandom();
		for (int i = 0; i < 12; i++) {
			double theta = random.nextDouble() * Math.PI * 2;
			double y = random.nextDouble() * 2 - 1;
			double ring = Math.sqrt(1 - y * y);
			field.level.sendParticles(new DustParticleOptions(TIME_BUBBLE.color, 1.0f), c.x + Math.cos(theta) * ring * r, c.y + y * r, c.z + Math.sin(theta) * ring * r, 1, 0, 0, 0, 0);
		}
	}

	/** Goop Suck Gus: every enemy inside the goop cloud loses its armor once, and each one adds a level of absorption to the shooter. */
	private static void tickGoopStrip(Field field, long now) {
		Vec3 c = field.center;
		double r = GOOP_STRIP.radius;
		var random = field.level.getRandom();
		for (int i = 0; i < 6; i++) {
			Vec3 p = c.add((random.nextDouble() - 0.5) * r * 1.6, (random.nextDouble() - 0.3) * 1.5, (random.nextDouble() - 0.5) * r * 1.6);
			field.level.sendParticles(new DustParticleOptions(GOOP_STRIP.color, 1.6f), p.x, p.y, p.z, 1, 0, 0, 0, 0);
		}
		if ((now - field.start) % 5 != 0)
			return;
		for (LivingEntity target : targetsAround(field.level, c, r, field.owner)) {
			if (STRIPPED.containsKey(target) || target instanceof Player || isPossessed(target) || !(target instanceof Enemy || target instanceof Mob mob && mob.getTarget() == field.owner))
				continue;
			strip(field, target, now);
		}
	}

	private static void strip(Field field, LivingEntity target, long now) {
		// worn armor is sucked away for good, the armor value is gone until STRIP_TICKS pass
		for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET})
			target.setItemSlot(slot, ItemStack.EMPTY);
		var armor = target.getAttribute(Attributes.ARMOR);
		if (armor != null && !armor.hasModifier(STRIPPED_ARMOR))
			armor.addTransientModifier(new AttributeModifier(STRIPPED_ARMOR, -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		STRIPPED.put(target, now + STRIP_TICKS);
		target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 1));
		Vec3 from = target.position().add(0, target.getBbHeight() / 2, 0);
		field.level.sendParticles(ParticleTypes.ITEM_SLIME, from.x, from.y, from.z, 12, 0.3, 0.4, 0.3, 0.1);
		field.level.playSound(null, from.x, from.y, from.z, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.PLAYERS, 1.2f, 0.7f);
		if (field.owner instanceof LivingEntity owner) {
			// a goop trail from the enemy to the shooter
			Vec3 to = owner.position().add(0, owner.getBbHeight() / 2, 0);
			for (int i = 1; i <= 12; i++) {
				Vec3 p = from.lerp(to, i / 13.0);
				field.level.sendParticles(new DustParticleOptions(GOOP_STRIP.color, 1.0f), p.x, p.y, p.z, 1, 0, 0, 0, 0);
			}
			MobEffectInstance current = owner.getEffect(MobEffects.ABSORPTION);
			int level = current == null ? 0 : Math.min(current.getAmplifier() + 1, MAX_ABSORPTION_LEVEL - 1);
			owner.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ABSORPTION_TICKS, level));
			field.level.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 1.0f, 1.2f);
		}
	}

	/** Portal Sweezy: after a short delay the portal fires fully charged crystals at the nearest enemy (or straight ahead). */
	private static void tickPortal(Field field, long now) {
		Vec3 c = field.center;
		Vec3 aim = field.aim;
		// ring facing the shot direction
		Vec3 right = aim.cross(new Vec3(0, 1, 0));
		right = right.lengthSqr() < 1.0E-6 ? new Vec3(1, 0, 0) : right.normalize();
		Vec3 up = right.cross(aim).normalize();
		long age = now - field.start;
		double size = Math.min(1.0, (age + 1) / 8.0) * 0.9;
		for (int i = 0; i < 10; i++) {
			double a = (age * 0.35 + i * Math.PI * 2 / 10);
			Vec3 p = c.add(right.scale(Math.cos(a) * size)).add(up.scale(Math.sin(a) * size));
			field.level.sendParticles(new DustParticleOptions(PORTAL.color, 1.2f), p.x, p.y, p.z, 1, 0, 0, 0, 0);
		}
		field.level.sendParticles(ParticleTypes.PORTAL, c.x, c.y, c.z, 3, 0.2, 0.2, 0.2, 0.3);
		if (age < PORTAL_DELAY || (age - PORTAL_DELAY) % PORTAL_INTERVAL != 0 || !(field.owner instanceof LivingEntity owner))
			return;
		GunSpec spec = GunSpec.get(new ItemStack(McholModItems.PORTAL_SWEEZY.get()));
		if (spec == null)
			return;
		Vec3 direction = aim;
		LivingEntity target = null;
		double best = Double.MAX_VALUE;
		for (LivingEntity candidate : targetsAround(field.level, c, PORTAL.radius, owner)) {
			if (candidate instanceof Player || isPossessed(candidate) || !(candidate instanceof Enemy || candidate instanceof Mob mob && mob.getTarget() == owner))
				continue;
			double d = candidate.position().distanceToSqr(c);
			if (d < best) {
				best = d;
				target = candidate;
			}
		}
		if (target != null)
			direction = target.position().add(0, target.getBbHeight() * 0.6, 0).subtract(c);
		var crystal = GunHandler.spawnProjectile(owner, spec, direction, 3.5f, 0, PORTAL_DAMAGE, 40, false);
		crystal.setPos(c.x, c.y, c.z);
		crystal.setCritArrow(true);
		field.level.sendParticles(new DustParticleOptions(PORTAL.color, 2.0f), c.x, c.y, c.z, 12, 0.25, 0.25, 0.25, 0);
		field.level.playSound(null, c.x, c.y, c.z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.5f, 0.6f);
		SoundEvent shot = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(spec.sound()));
		if (shot != null)
			field.level.playSound(null, c.x, c.y, c.z, shot, SoundSource.PLAYERS, 1.2f, 0.6f);
	}

	@Nullable
	private static LivingEntity nearestEnemy(ServerLevel level, Mob mob) {
		LivingEntity best = null;
		double bestDistance = Double.MAX_VALUE;
		for (LivingEntity candidate : level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(POSSESSION.radius), e -> e != mob && e.isAlive() && e instanceof Enemy && !POSSESSED.containsKey(e))) {
			double distance = candidate.distanceToSqr(mob);
			if (distance < bestDistance) {
				bestDistance = distance;
				best = candidate;
			}
		}
		return best;
	}

	/** Fields keep a reference to their level, so they must not survive leaving a singleplayer world. */
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		FIELDS.clear();
		STRIPPED.clear();
		POSSESSED.clear();
	}

	/** A possessed mob must not go back to attacking players. */
	@SubscribeEvent
	public static void onChangeTarget(LivingChangeTargetEvent event) {
		if (event.getEntity() instanceof Mob mob && POSSESSED.containsKey(mob) && event.getNewAboutToBeSetTarget() instanceof Player)
			event.setCanceled(true);
	}
}
