package net.mcreator.mchol.jetpack;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;

import net.mcreator.mchol.McholMod;

/**
 * Bounty Hunter jetpack flight. Double tap jump in the air to turn it on (or off again):
 * hold jump to fly up, release to hover and slowly sink, sneak to drop. Landing turns it off.
 */
@EventBusSubscriber(Dist.CLIENT)
public class JetpackClient {
	/** Max ticks between the two jump presses, same as vanilla creative flight. */
	private static final int DOUBLE_TAP_TICKS = 7;
	private static final float MAX_FUEL = 100;
	// nerfed 2026-11-19: it stayed in the air far too long (was 5 s thrust / ~14 s hover, 0.6 climb speed)
	private static final float THRUST_COST = 1.7f; // ~3 seconds of thrust on a full tank
	private static final float HOVER_COST = 0.9f; // ~5.5 seconds of hovering on a full tank
	private static final float REFUEL_RATE = 1.5f;
	private static final int REFUEL_DELAY = 30; // ticks on the ground before refueling starts
	/** Fastest climb speed while thrusting (blocks per tick, before vanilla gravity/drag). */
	private static final double MAX_CLIMB = 0.42;
	/** Hovering doesn't hold you in place, you sink: about 2.7 blocks per second. */
	private static final double HOVER_SINK = -0.06;

	private static boolean active;
	private static float fuel = MAX_FUEL;
	private static int groundTicks;
	private static boolean jumpWasDown;
	private static int lastJumpPress = -100;
	private static int sentState = JetpackStateMessage.OFF;

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.HOTBAR, Identifier.fromNamespaceAndPath(McholMod.MODID, "jetpack_fuel"), JetpackClient::renderFuel);
	}

	private static void renderFuel(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.gui.hud.isHidden() || !Jetpack.wearsJetpack(mc.player) || (!active && fuel >= MAX_FUEL))
			return;
		// vertical gauge right of the hotbar
		int x = graphics.guiWidth() / 2 + 94;
		int bottom = graphics.guiHeight() - 2;
		int height = 20;
		int filled = Math.round(height * fuel / MAX_FUEL);
		graphics.fill(x, bottom - height - 1, x + 5, bottom + 1, 0xA0000000);
		graphics.fill(x + 1, bottom - filled, x + 4, bottom, fuel < MAX_FUEL * 0.25f ? 0xFFE23C3C : 0xFFFF9A2E);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class GameEvents {
		@SubscribeEvent
		public static void onPlayerTick(PlayerTickEvent.Pre event) {
			if (!(event.getEntity() instanceof LocalPlayer player) || player != Minecraft.getInstance().player)
				return;
			boolean canFly = Jetpack.wearsJetpack(player) && !player.getAbilities().flying && !player.isFallFlying() && !player.isInWater() && !player.isPassenger() && !player.isSpectator();
			boolean jumpDown = player.input.keyPresses.jump();
			if (jumpDown && !jumpWasDown) {
				if (player.tickCount - lastJumpPress <= DOUBLE_TAP_TICKS && !player.onGround() && canFly && (active || fuel > MAX_FUEL * 0.1f)) {
					active = !active;
					lastJumpPress = -100;
				} else {
					lastJumpPress = player.tickCount;
				}
			}
			jumpWasDown = jumpDown;
			if (!canFly || player.onGround())
				active = false;

			boolean thrust = false;
			if (active) {
				thrust = jumpDown && fuel > 0;
				fly(player, thrust);
				fuel -= thrust ? THRUST_COST : HOVER_COST;
				if (fuel <= 0) {
					fuel = 0;
					active = false;
				}
			} else if (player.onGround()) {
				if (++groundTicks > REFUEL_DELAY)
					fuel = Math.min(MAX_FUEL, fuel + REFUEL_RATE);
			}
			if (!player.onGround())
				groundTicks = 0;

			int state = !active ? JetpackStateMessage.OFF : thrust ? JetpackStateMessage.THRUST : JetpackStateMessage.HOVER;
			if (state != sentState) {
				ClientPacketDistributor.sendToServer(new JetpackStateMessage(state));
				sentState = state;
			}
		}

		private static void fly(LocalPlayer player, boolean thrust) {
			Vec3 motion = player.getDeltaMovement();
			double vy = motion.y;
			// vanilla then applies gravity (-0.08) and drag (x0.98) to these values
			if (thrust)
				vy = Math.min(vy + 0.15, MAX_CLIMB);
			else if (!player.isShiftKeyDown())
				vy = Math.max(vy, HOVER_SINK);

			// extra air control in the direction of the movement keys
			double vx = motion.x;
			double vz = motion.z;
			Vec2 move = player.input.getMoveVector();
			if (move.lengthSquared() > 0) {
				float sin = Mth.sin(player.getYRot() * Mth.DEG_TO_RAD);
				float cos = Mth.cos(player.getYRot() * Mth.DEG_TO_RAD);
				double boost = thrust ? 0.035 : 0.025;
				vx += (move.x * cos - move.y * sin) * boost;
				vz += (move.y * cos + move.x * sin) * boost;
				double horizontal = Math.sqrt(vx * vx + vz * vz);
				if (horizontal > 0.7) {
					vx *= 0.7 / horizontal;
					vz *= 0.7 / horizontal;
				}
			}
			player.setDeltaMovement(vx, vy, vz);
			player.resetFallDistance();
		}
	}
}
