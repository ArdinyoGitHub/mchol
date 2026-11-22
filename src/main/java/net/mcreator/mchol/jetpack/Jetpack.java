package net.mcreator.mchol.jetpack;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.mchol.init.McholModItems;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Bounty Hunter jetpack, server side: no fall damage while flying, no "kicked for floating" and the exhaust effects.
 * The flight itself is client side (see JetpackClient) because player movement is computed by the client.
 */
@EventBusSubscriber
public class Jetpack {
	private static final Map<UUID, Integer> STATES = new HashMap<>();

	public static boolean wearsJetpack(Player player) {
		return player.getItemBySlot(EquipmentSlot.CHEST).is(McholModItems.BOUNTY_HUNTER_CHESTPLATE.get());
	}

	static void setState(ServerPlayer player, int state) {
		if (state == JetpackStateMessage.OFF || !wearsJetpack(player))
			STATES.remove(player.getUUID());
		else
			STATES.put(player.getUUID(), state);
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player) || !(player.level() instanceof ServerLevel level))
			return;
		Integer state = STATES.get(player.getUUID());
		if (state == null)
			return;
		if (!wearsJetpack(player) || player.isSpectator()) {
			STATES.remove(player.getUUID());
			return;
		}
		player.resetFallDistance();
		player.connection.resetFlyingTicks();

		// two nozzles on the back
		double yaw = Math.toRadians(player.getYRot());
		Vec3 back = new Vec3(Math.sin(yaw), 0, -Math.cos(yaw)).scale(0.35);
		Vec3 side = new Vec3(Math.cos(yaw), 0, Math.sin(yaw)).scale(0.18);
		Vec3 base = player.position().add(back).add(0, 0.85, 0);
		boolean thrust = state == JetpackStateMessage.THRUST;
		for (Vec3 nozzle : new Vec3[]{base.add(side), base.subtract(side)}) {
			// count 0 makes the offsets act as the particle's velocity
			level.sendParticles(ParticleTypes.FLAME, nozzle.x, nozzle.y, nozzle.z, 0, 0, thrust ? -0.35 : -0.12, 0, 1);
			if (thrust)
				level.sendParticles(ParticleTypes.SMOKE, nozzle.x, nozzle.y - 0.2, nozzle.z, 0, 0, -0.2, 0, 1);
		}
		if (thrust && player.tickCount % 3 == 0)
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.15f, 1.8f + level.getRandom().nextFloat() * 0.2f);
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		STATES.remove(event.getEntity().getUUID());
	}
}
