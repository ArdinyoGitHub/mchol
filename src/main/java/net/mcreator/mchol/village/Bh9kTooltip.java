package net.mcreator.mchol.village;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import net.mcreator.mchol.init.McholModBlocks;

/**
 * Tooltip of the BH9K (Bounty Hunter 9000) block item. Written here instead of in the MCreator element so it can be
 * changed without opening MCreator.
 */
@EventBusSubscriber(value = Dist.CLIENT)
public class Bh9kTooltip {
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (!event.getItemStack().is(McholModBlocks.BH_9_K.get().asItem()))
			return;
		var lines = event.getToolTip();
		lines.add(Component.literal("Bounty Hunter 9000").withStyle(ChatFormatting.GREEN));
		lines.add(Component.literal("Place it and right click to teleport.").withStyle(ChatFormatting.GRAY));
		lines.add(Component.literal("Destinations: Zephyr Paradise, Gatlus, Port Terenne,").withStyle(ChatFormatting.GRAY));
		lines.add(Component.literal("Overworld, Nether, End").withStyle(ChatFormatting.GRAY));
	}
}
