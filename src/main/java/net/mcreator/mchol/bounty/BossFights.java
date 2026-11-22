package net.mcreator.mchol.bounty;

import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.Difficulty;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.DustParticleOptions;

import net.mcreator.mchol.merk.MerkGuns;
import net.mcreator.mchol.init.McholModItems;
import net.mcreator.mchol.guns.GunSpec;
import net.mcreator.mchol.guns.GunHandler;
import net.mcreator.mchol.guns.AltFire;
import net.mcreator.mchol.entity.GlobEntity;
import net.mcreator.mchol.McholMod;

import java.util.function.Supplier;
import java.util.WeakHashMap;
import java.util.Map;

/**
 * Bounty bosses, as in High on Life: Krubis carries Gus (Zephyr Paradise), Douglas carries Sweezy (Port Terenne).
 * They shoot their gun like the player does (alt-fire included) and always drop it. Natural spawns come from the biome modifiers in the
 * built-in pack (packs/zephyr_trades) and are thinned out here so a boss stays a rare encounter.
 */
@EventBusSubscriber
public class BossFights {
	/** Boss shots hit a bit softer than the player's (Merks use 0.75). */
	private static final double DAMAGE_SCALE = 0.8;
	private static final float EXTRA_INACCURACY = 1.0f;
	/** A boss never spawns naturally within this many blocks of another boss of the same kind. */
	private static final double MIN_DISTANCE = 160;
	/** Share of the spawn attempts that pass every other check which are actually allowed. */
	private static final float SPAWN_CHANCE = 0.35f;
	/** Ticks after the first attack before a boss may use its gun's alt-fire. */
	private static final int FIRST_ALT_DELAY = 60;
	/** Bosses wait a bit longer than the player between two alt-fires. */
	private static final double ALT_COOLDOWN_SCALE = 1.25;
	/** Boss -> game time when its alt-fire is ready again. */
	private static final Map<Mob, Long> NEXT_ALT = new WeakHashMap<>();

	private enum Boss {
		KRUBIS(BountyEntities.KRUBIS, () -> McholModItems.GUS.get(), "zephyr_paradise", 1),
		// Sweezy is a needle gun: three needles per attack so Douglas keeps up pressure
		DOUGLAS(BountyEntities.DOUGLAS, () -> McholModItems.SWEEZY.get(), "port_terenne", 3);

		final BountyEntities entity;
		final Supplier<Item> gun;
		final ResourceKey<Level> home;
		final int volleys;

		Boss(BountyEntities entity, Supplier<Item> gun, String home, int volleys) {
			this.entity = entity;
			this.gun = gun;
			this.home = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(McholMod.MODID, home));
			this.volleys = volleys;
		}

		static Boss of(net.minecraft.world.entity.Entity e) {
			for (Boss boss : values())
				if (boss.entity.is(e))
					return boss;
			return null;
		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide())
			return;
		if (event.getEntity() instanceof Mob mob && Boss.of(mob) instanceof Boss boss) {
			if (mob.getMainHandItem().isEmpty())
				mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(boss.gun.get()));
			// above 1 the item drops every time, even when the boss was not killed by a player
			mob.setDropChance(EquipmentSlot.MAINHAND, 2.0f);
		} else if (event.getEntity() instanceof GlobEntity glob && !glob.entityTags().contains(GunHandler.PROJECTILE_TAG) && glob.getOwner() instanceof Mob owner
				&& Boss.of(owner) instanceof Boss boss) {
			// the generated performRangedAttack always fires a plain glob: swap it for the held gun's shot
			GunSpec spec = GunSpec.get(owner.getMainHandItem());
			if (spec == null)
				return;
			event.setCanceled(true);
			if (!tryAltFire(owner, spec))
				MerkGuns.fireHeldGun(owner, spec, glob.getDeltaMovement(), DAMAGE_SCALE, EXTRA_INACCURACY, boss.volleys);
		}
	}

	/**
	 * Uses the held gun's alt-fire (right click ability) instead of a normal attack when it is ready: Krubis' Gus throws a
	 * Vortex that pulls the player in, Douglas' Sweezy a Time Bubble that slows the player down.
	 */
	private static boolean tryAltFire(Mob boss, GunSpec spec) {
		AltFire alt = AltFire.get(boss.getMainHandItem());
		LivingEntity target = boss.getTarget();
		if (alt == null || target == null || !(boss.level() instanceof ServerLevel level))
			return false;
		long now = level.getGameTime();
		Long ready = NEXT_ALT.get(boss);
		if (ready == null) {
			// not right at the start of the fight
			NEXT_ALT.put(boss, now + FIRST_ALT_DELAY);
			return false;
		}
		double distance = boss.distanceTo(target);
		// the ability shot flies speed * lifetime blocks, aim only when it can reach the target
		if (now < ready || distance > alt.speed * alt.lifetime * 0.9 || distance < 3 || !boss.hasLineOfSight(target))
			return false;
		NEXT_ALT.put(boss, now + (long) (alt.cooldown * ALT_COOLDOWN_SCALE));
		Vec3 aim = new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ()).subtract(boss.getEyePosition());
		alt.fire(boss, spec, aim);
		// a short warning so the player can react
		level.sendParticles(new DustParticleOptions(alt.color, 1.5f), boss.getX(), boss.getEyeY(), boss.getZ(), 20, 0.5, 0.5, 0.5, 0);
		return true;
	}

	@SubscribeEvent
	public static void onSpawnPositionCheck(MobSpawnEvent.PositionCheck event) {
		Boss boss = Boss.of(event.getEntity());
		if (boss == null || (event.getSpawnType() != EntitySpawnReason.NATURAL && event.getSpawnType() != EntitySpawnReason.CHUNK_GENERATION))
			return;
		Mob mob = event.getEntity();
		Level level = event.getLevel().getLevel();
		boolean allowed = level.dimension() == boss.home && mob.getRandom().nextFloat() < SPAWN_CHANCE && mob.checkSpawnObstruction(event.getLevel())
				&& level.getEntities(mob.getType(), new AABB(mob.blockPosition()).inflate(MIN_DISTANCE), e -> e != mob).isEmpty();
		event.setResult(allowed ? MobSpawnEvent.PositionCheck.Result.SUCCEED : MobSpawnEvent.PositionCheck.Result.FAIL);
	}

	@EventBusSubscriber
	public static class ModEvents {
		/** The elements register no placement (spawnThisMob is off). Bosses spawn on the ground in any light. */
		@SuppressWarnings("unchecked")
		@SubscribeEvent
		public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
			for (Boss boss : Boss.values()) {
				EntityType<Mob> type = (EntityType<Mob>) boss.entity.type();
				if (type == null)
					continue;
				event.register(type, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						(t, level, reason, pos, random) -> level.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(t, level, reason, pos, random),
						RegisterSpawnPlacementsEvent.Operation.REPLACE);
			}
		}
	}
}
