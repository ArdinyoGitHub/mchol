package net.mcreator.mchol.guns;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

/**
 * Lezduit's voice was broken (by Dr. Giblets' experiments or by Gene's repair), so the only thing he can say is his own name:
 * when a player takes him in hand and sometimes after a kill.
 */
@EventBusSubscriber
public class LezduitVoice {
	private static final Style NAME = Style.EMPTY.withColor(TextColor.fromRgb(0xFF5AE0)).withBold(true);
	private static final String[] LINES = {"Lezduit.", "Lezduit!", "Lezduit...", "LEZDUIT!", "Lez... duit."};
	/** Chance that a kill makes him talk. */
	private static final float KILL_CHANCE = 0.3f;

	/** Players currently holding Lezduit, so he speaks once when taken in hand, not every tick. */
	private static final Set<UUID> HOLDING = new HashSet<>();

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;
		if (LezduitBeam.isLezduit(player)) {
			if (HOLDING.add(player.getUUID()))
				say(player);
		} else {
			HOLDING.remove(player.getUUID());
		}
	}

	static void onKill(ServerPlayer player) {
		if (player.getRandom().nextFloat() < KILL_CHANCE)
			say(player);
	}

	private static void say(ServerPlayer player) {
		String line = LINES[player.getRandom().nextInt(LINES.length)];
		player.sendSystemMessage(Component.literal("<").append(Component.literal("Lezduit").withStyle(NAME)).append("> " + line));
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_AMBIENT, SoundSource.PLAYERS, 0.5f, 0.7f);
	}
}
