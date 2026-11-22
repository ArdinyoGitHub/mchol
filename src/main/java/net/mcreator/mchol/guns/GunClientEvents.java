package net.mcreator.mchol.guns;


import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import com.mojang.blaze3d.platform.InputConstants;

/** Client side input: left click fires (held = automatic), right click alt-fires, R reloads. Also drives the recoil animation. */
@EventBusSubscriber(Dist.CLIENT)
public class GunClientEvents {
	public static final KeyMapping RELOAD = new KeyMapping("key.mchol.reload", InputConstants.KEY_R, KeyMapping.Category.GAMEPLAY);

	private static final float RECOIL_SECONDS = 0.25f;
	private static long lastShotNanos;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(RELOAD);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new GunItemExtensions(), GunSpec.items());
	}

	/** Called when the server confirms a shot. */
	public static void onShot() {
		lastShotNanos = System.nanoTime();
	}

	/** 1 right after a shot, easing back to 0. */
	public static float recoilAmount() {
		float t = (System.nanoTime() - lastShotNanos) / 1.0e9f / RECOIL_SECONDS;
		return t >= 1 ? 0 : (1 - t) * (1 - t);
	}

	private static boolean holdingGun(Minecraft mc) {
		return mc.player != null && GunSpec.get(mc.player.getMainHandItem()) != null;
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class GameEvents {
		/** Left click fires instead of breaking blocks/hitting mobs, right click uses the alt-fire. */
		@SubscribeEvent
		public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
			Minecraft mc = Minecraft.getInstance();
			if (!holdingGun(mc))
				return;
			if (event.isAttack()) {
				event.setCanceled(true);
				event.setSwingHand(false);
			} else if (event.isUseItem() && event.getHand() == InteractionHand.MAIN_HAND && !aimingAtUsableBlock(mc)) {
				event.setCanceled(true);
				event.setSwingHand(false);
				ClientPacketDistributor.sendToServer(new GunMessage(GunMessage.ALT_FIRE));
			}
		}

		/** Doors, chests, buttons etc. keep working with a gun in hand. */
		private static boolean aimingAtUsableBlock(Minecraft mc) {
			if (!(mc.hitResult instanceof BlockHitResult hit) || hit.getType() != HitResult.Type.BLOCK || mc.level == null)
				return false;
			BlockState state = mc.level.getBlockState(hit.getBlockPos());
			Block block = state.getBlock();
			return state.getMenuProvider(mc.level, hit.getBlockPos()) != null || block instanceof BaseEntityBlock || block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock
					|| block instanceof ButtonBlock || block instanceof LeverBlock || block instanceof BedBlock;
		}

		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			Minecraft mc = Minecraft.getInstance();
			while (RELOAD.consumeClick()) {
				if (mc.gui.screen() == null && holdingGun(mc))
					ClientPacketDistributor.sendToServer(new GunMessage(GunMessage.RELOAD));
			}
			// the server enforces the fire rate, so the trigger state is simply sent every tick
			if (mc.gui.screen() == null && mc.mouseHandler.isMouseGrabbed() && mc.options.keyAttack.isDown() && holdingGun(mc))
				ClientPacketDistributor.sendToServer(new GunMessage(GunMessage.FIRE));
		}

		/** Visual camera kick; the real aim direction is not changed. */
		@SubscribeEvent
		public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null || !mc.options.getCameraType().isFirstPerson())
				return;
			GunSpec spec = GunSpec.get(mc.player.getMainHandItem());
			if (spec != null)
				event.setPitch(event.getPitch() - recoilAmount() * spec.recoil() * 0.6f);
		}
	}
}
