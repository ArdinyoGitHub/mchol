package net.mcreator.mchol.guns;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

/** Marks a broken Lezduit (dropped by Merks) in its tooltip. */
@EventBusSubscriber(value = Dist.CLIENT)
public class LezduitTooltip {
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (!LezduitRepair.isBroken(event.getItemStack()))
			return;
		event.getToolTip().add(Component.translatableWithFallback("tooltip.mchol.lezduit_broken", "Broken").withStyle(ChatFormatting.RED));
		event.getToolTip().add(Component.translatableWithFallback("tooltip.mchol.lezduit_broken_hint", "Gene can repair him.").withStyle(ChatFormatting.GRAY));
	}
}
