package net.mcreator.mchol.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;

public class BountyHunterBootsTickEventProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		itemstack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING), 10);
	}
}