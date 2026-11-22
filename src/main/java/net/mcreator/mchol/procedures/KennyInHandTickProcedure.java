package net.mcreator.mchol.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Prediction;

import net.mcreator.mchol.init.McholModItems;
import net.mcreator.mchol.McholMod;

public class KennyInHandTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!hasEntityInInventory(entity, new ItemStack(McholModItems.GATTERALTYPE_3.get()))) {
			McholMod.queueServerWork(125, () -> {
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(McholModItems.GATTERALTYPE_3.get()).copy();
					_setstack.setCount(10);
					_player.getInventory().placeItemBackInInventory(_setstack, Prediction.SERVER_ONLY);
				}
			});
		}
	}

	private static boolean hasEntityInInventory(Entity entity, ItemStack itemstack) {
		if (entity instanceof Player player)
			return player.getInventory().contains(stack -> !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack));
		return false;
	}
}