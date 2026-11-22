package net.mcreator.mchol.jetpack;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import net.mcreator.mchol.init.McholModItems;

/** Tooltip of the Bounty Hunter Chestplate: it carries the jetpack backpack (see JetpackClient). Kept out of the MCreator element. */
@EventBusSubscriber(value = Dist.CLIENT)
public class JetpackTooltip {
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (!event.getItemStack().is(McholModItems.BOUNTY_HUNTER_CHESTPLATE.get()))
			return;
		var lines = event.getToolTip();
		lines.add(Component.translatableWithFallback("tooltip.mchol.backpack", "Backpack").withStyle(ChatFormatting.GOLD));
		lines.add(Component.translatableWithFallback("tooltip.mchol.backpack_hint", "Double tap jump in the air to fly.").withStyle(ChatFormatting.GRAY));
		lines.add(Component.translatableWithFallback("tooltip.mchol.backpack_hint2", "Hold jump to climb, sneak to drop. Refuels on the ground.").withStyle(ChatFormatting.GRAY));
	}
}
