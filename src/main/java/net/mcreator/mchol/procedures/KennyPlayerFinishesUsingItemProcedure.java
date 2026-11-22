package net.mcreator.mchol.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

public class KennyPlayerFinishesUsingItemProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity instanceof Player _player) {
			// Check if we need to reload (5 shots or ammo empty)
			double shotsFired = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("shotsFired", 0);
			double ammo = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("ammo", 10);
			
			if (shotsFired >= 5 || ammo <= 0) {
				// Reload: 60 ticks (3 seconds) cooldown
				_player.getCooldowns().addCooldown(itemstack, 60);
				// Reset shotsFired
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble("shotsFired", 0));
				// Reset ammo to 10
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble("ammo", 10));
			} else {
				// Normal cooldown (20 ticks = 1 second)
				_player.getCooldowns().addCooldown(itemstack, 20);
			}
		}
	}
}