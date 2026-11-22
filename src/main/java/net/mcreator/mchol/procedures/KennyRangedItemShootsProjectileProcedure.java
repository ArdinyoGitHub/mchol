package net.mcreator.mchol.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

public class KennyRangedItemShootsProjectileProcedure {
	public static void execute(ItemStack itemstack) {
		if (!(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("ammo", 0) < 1)) {
			{
				final String _tagName = "ammo";
				final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("ammo", 0) - 1);
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
			}
		}
		// Increment shotsFired
		double shotsFired = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("shotsFired", 0);
		CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble("shotsFired", shotsFired + 1));
	}
}