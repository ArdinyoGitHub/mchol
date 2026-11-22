/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.mchol.network.JetpackMessage;

import com.mojang.blaze3d.platform.InputConstants;

@EventBusSubscriber(Dist.CLIENT)
public class McholModKeyMappings {
	public static final KeyMapping JETPACK = new KeyMapping("key.mchol.jetpack",

			InputConstants.KEY_G, KeyMapping.Category.MISC) {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPacketDistributor.sendToServer(new JetpackMessage(0, 0));
				JetpackMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				JETPACK_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - JETPACK_LASTPRESS);
				ClientPacketDistributor.sendToServer(new JetpackMessage(1, dt));
				JetpackMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long JETPACK_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(JETPACK);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().gui.screen() == null) {
				JETPACK.consumeClick();
			}
		}
	}
}