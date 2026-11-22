package net.mcreator.mchol.guns;

import net.minecraft.world.level.Level;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.mchol.init.McholModItems;
import net.mcreator.mchol.init.McholModEntities;
import net.mcreator.mchol.entity.GusGlobEntity;
import net.mcreator.mchol.entity.GlobEntity;
import net.mcreator.mchol.entity.Glob2Entity;
import net.mcreator.mchol.entity.CrystalEntity;
import net.mcreator.mchol.entity.CreatureChildrenEntity;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;

/**
 * Stats of each gun. All times are in ticks (20 ticks = 1 second).
 *
 * @param magazine     shots before a reload is needed
 * @param fireInterval ticks between two shots while the trigger is held
 * @param reloadTicks  reload duration
 * @param pellets      projectiles per shot (shotgun > 1)
 * @param inaccuracy   spread, 0 = laser accurate (vanilla bow uses 1)
 * @param speed        projectile speed in blocks per tick
 * @param damage       damage per pellet (2 = one heart)
 * @param lifetime     ticks before a projectile that hit nothing disappears (limits range)
 * @param gravity      whether the projectile falls like an arrow
 * @param sound        sound played on each shot
 * @param recoil       strength of the first person kick (camera and gun model)
 * @param muzzleColor  RGB color of the muzzle flash particles
 */
public record GunSpec(int magazine, int fireInterval, int reloadTicks, int pellets, float inaccuracy, float speed, double damage, int lifetime, boolean gravity, String sound, float recoil, int muzzleColor,
		ProjectileFactory projectile) {
	/** Guns without a projectile (null factory) call down Lezduit's lightning storm instead, see LezduitBeam. */
	public interface ProjectileFactory {
		AbstractArrow create(Level level, LivingEntity shooter);
	}

	private static Map<Item, GunSpec> specs;

	private static Map<Item, GunSpec> specs() {
		if (specs == null) {
			specs = new HashMap<>(Map.of(
					// Kenny: reliable pistol, medium fire rate
					McholModItems.KENNY.get(), new GunSpec(12, 4, 30, 1, 1.0f, 3.5f, 4, 40, false, "mchol:kennyshot", 2.5f, 0x9BE23C, (level, shooter) -> new GlobEntity(McholModEntities.GLOB.get(), shooter, level, null)),
					// Multi-Glob Kenny (yellow): same pistol, the alt-fire splits into several globs
					McholModItems.MULTI_GLOB_KENNY.get(), new GunSpec(12, 4, 30, 1, 1.0f, 3.5f, 4, 40, false, "mchol:kennyshot", 2.5f, 0xFFE53B, (level, shooter) -> new Glob2Entity(McholModEntities.GLOB_2.get(), shooter, level, null)),
					// Gus: shotgun, strong but short range and slow
					McholModItems.GUS.get(), new GunSpec(4, 16, 45, 8, 9.0f, 2.8f, 2.5, 8, false, "mchol:gushot", 7.0f, 0x7FD13B, (level, shooter) -> new GusGlobEntity(McholModEntities.GUS_GLOB.get(), shooter, level, null)),
					// Goop Suck Gus: same shotgun, its alt-fire strips armor instead of pulling (AltFire.GOOP_STRIP)
					McholModItems.GOOP_SUCK_GUS.get(), new GunSpec(4, 16, 45, 8, 9.0f, 2.8f, 2.5, 8, false, "mchol:gushot", 7.0f, 0x4FE08A, (level, shooter) -> new GusGlobEntity(McholModEntities.GUS_GLOB.get(), shooter, level, null)),
					// Portal Sweezy: same needle gun, its alt-fire opens a Summoning Portal (AltFire.PORTAL)
					McholModItems.PORTAL_SWEEZY.get(), new GunSpec(30, 2, 40, 1, 3.0f, 3.0f, 2, 30, false, "mchol:sweezyshot", 0.8f, 0xB05CFF, (level, shooter) -> new CrystalEntity(McholModEntities.CRYSTAL.get(), shooter, level, null)),
					// Sweezy: fully automatic needle gun
					McholModItems.SWEEZY.get(), new GunSpec(30, 2, 40, 1, 3.0f, 3.0f, 2, 30, false, "mchol:sweezyshot", 0.8f, 0x6FD5FF, (level, shooter) -> new CrystalEntity(McholModEntities.CRYSTAL.get(), shooter, level, null)),
					// Creature: lobs its children in an arc
					McholModItems.CREATUREE.get(), new GunSpec(15, 6, 40, 1, 2.0f, 2.0f, 3, 60, true, "mchol:creatureshot", 2.0f, 0xFF9A2E,
							(level, shooter) -> new CreatureChildrenEntity(McholModEntities.CREATURE_CHILDREN.get(), shooter, level, null))));
			// Lezduit: 5 s of automatic lightning (25 shots, one every 4 ticks) on every mob on screen (5 damage each per shot), then a 20 s recharge (reload = cooldown), no alt-fire. Looked up by id so this compiles before MCreator has generated the item.
			BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("mchol", "lezduit"))
					.ifPresent(item -> specs.put(item, new GunSpec(25, 4, 400, 1, 0, 0, 5, 0, false, "minecraft:block.beacon.power_select", 1.2f, 0xFF5AE0, null)));
		}
		return specs;
	}

	@Nullable
	public static GunSpec get(ItemStack stack) {
		if (stack.isEmpty())
			return null;
		return specs().get(stack.getItem());
	}

	public static Item[] items() {
		return specs().keySet().toArray(Item[]::new);
	}
}
