package net.mcreator.mchol.merk;

import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.mchol.init.McholModItems;
import net.mcreator.mchol.init.McholModEntities;
import net.mcreator.mchol.guns.GunSpec;
import net.mcreator.mchol.guns.GunHandler;
import net.mcreator.mchol.entity.MerkEntity;
import net.mcreator.mchol.entity.GlobEntity;

/**
 * Merks spawn holding one of the four base guns and shoot that gun's projectile instead of the plain glob.
 * The gun drops with the vanilla equipment chance (8.5%, only when killed by a player); a broken Lezduit drops at 1% (guns/LezduitRepair).
 * Lives outside the MCreator generated files, so regenerating the code keeps it.
 */
@EventBusSubscriber
public class MerkGuns {
	/** Merks hit softer than a player holding the same gun. */
	private static final double MOB_DAMAGE_SCALE = 0.75;
	/** Extra spread so Merks don't snipe like a player. */
	private static final float MOB_EXTRA_INACCURACY = 2.0f;

	private static Item[] guns() {
		return new Item[]{McholModItems.KENNY.get(), McholModItems.GUS.get(), McholModItems.SWEEZY.get(), McholModItems.CREATUREE.get()};
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide() || event.loadedFromDisk())
			return;
		if (event.getEntity() instanceof MerkEntity merk) {
			if (merk.getMainHandItem().isEmpty()) {
				Item[] guns = guns();
				merk.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(guns[merk.getRandom().nextInt(guns.length)]));
			}
		} else if (event.getEntity() instanceof GlobEntity glob && !glob.entityTags().contains(GunHandler.PROJECTILE_TAG) && glob.getOwner() instanceof MerkEntity merk) {
			// MerkEntity.performRangedAttack (MCreator generated) always fires a glob: swap it for the held gun's shot
			GunSpec spec = GunSpec.get(merk.getMainHandItem());
			if (spec == null)
				return;
			event.setCanceled(true);
			fireHeldGun(merk, spec, glob.getDeltaMovement(), MOB_DAMAGE_SCALE, MOB_EXTRA_INACCURACY, 1);
		}
	}

	/**
	 * Fires the shooter's held gun at its target, as a player would (same projectile, pellets and sound). Also used by the bounty bosses.
	 *
	 * @param fallbackDirection direction to use when the shooter has no target
	 * @param damageScale       multiplier on the gun's damage per pellet
	 * @param extraInaccuracy   spread added on top of the gun's own
	 * @param volleys           how many times the whole shot is fired at once (the needle gun Sweezy fires single needles)
	 */
	public static void fireHeldGun(Mob shooter, GunSpec spec, Vec3 fallbackDirection, double damageScale, float extraInaccuracy, int volleys) {
		Vec3 direction = fallbackDirection;
		LivingEntity target = shooter.getTarget();
		if (target != null) {
			Vec3 from = shooter.getEyePosition();
			Vec3 to = new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.6, target.getZ());
			direction = to.subtract(from);
			if (spec.gravity())
				direction = direction.add(0, Math.hypot(direction.x, direction.z) * 0.2, 0);
		}
		if (direction.lengthSqr() < 1.0E-6)
			return;
		for (int i = 0; i < spec.pellets() * volleys; i++)
			GunHandler.spawnProjectile(shooter, spec, direction, spec.speed(), spec.inaccuracy() + extraInaccuracy, spec.damage() * damageScale, spec.lifetime(), spec.gravity());
		// GlobEntity.shoot already played the Kenny sound, add the gun's own sound for the others
		if (!"mchol:kennyshot".equals(spec.sound())) {
			SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(spec.sound()));
			if (sound != null)
				shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), sound, SoundSource.HOSTILE, 1, 0.9f + shooter.getRandom().nextFloat() * 0.2f);
		}
	}

	/**
	 * Merks are monsters, so by default they only spawn in the dark. Outside the overworld (Zephyr Paradise, Gatlus, Port Terenne;
	 * Nether and End biomes never list Merks) let them spawn in daylight too. In the overworld they stay rare and dark-only.
	 */
	@SubscribeEvent
	public static void onSpawnPositionCheck(MobSpawnEvent.PositionCheck event) {
		if (event.getEntity() instanceof MerkEntity merk && event.getLevel().getLevel().dimension() != Level.OVERWORLD && merk.checkSpawnObstruction(event.getLevel()))
			event.setResult(MobSpawnEvent.PositionCheck.Result.SUCCEED);
	}

	@EventBusSubscriber
	public static class ModEvents {
		/** MerkEntity.init registers no placement (NeoForge warns about it). Overworld: normal monster rules (dark only); other dimensions: any light. */
		@SubscribeEvent
		public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
			event.register(McholModEntities.MERK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					(type, level, reason, pos, random) -> level.getLevel().dimension() == Level.OVERWORLD
							? Monster.checkMonsterSpawnRules(type, level, reason, pos, random)
							: level.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(type, level, reason, pos, random),
					RegisterSpawnPlacementsEvent.Operation.REPLACE);
		}
	}
}
