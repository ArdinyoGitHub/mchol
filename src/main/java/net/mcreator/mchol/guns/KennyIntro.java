package net.mcreator.mchol.guns;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.mcreator.mchol.init.McholModItems;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * The first time a player holds Kenny, Kenny introduces himself in chat, one line every couple of seconds, like in High on Life.
 * Happens once per player (the flag survives death).
 */
@EventBusSubscriber
public class KennyIntro {
	private static final String SEEN_KEY = "mchol_kenny_intro";
	private static final int LINE_DELAY = 50;
	private static final String[] LINES = {
			"Did the Translation Microbes work? Great!",
			"My name is Kenny. I'm a talking gun.",
			"You must stop the G3 Cartel from harvesting the villagers.",
			"We must find Gene first.",
			"I heard he moved to Zephyr."};
	private static final Style NAME = Style.EMPTY.withColor(TextColor.fromRgb(0x9BE23C)).withBold(true);

	/** Player -> index of the next line to say. */
	private static final Map<UUID, Integer> TALKING = new HashMap<>();

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;
		Integer next = TALKING.get(player.getUUID());
		if (next == null) {
			if (player.getMainHandItem().is(McholModItems.KENNY.get()) && !player.getPersistentData().getBooleanOr(SEEN_KEY, false)) {
				player.getPersistentData().putBoolean(SEEN_KEY, true);
				TALKING.put(player.getUUID(), 0);
				say(player, 0);
			}
			return;
		}
		if (player.tickCount % LINE_DELAY != 0)
			return;
		int line = next + 1;
		if (line >= LINES.length) {
			TALKING.remove(player.getUUID());
			return;
		}
		TALKING.put(player.getUUID(), line);
		say(player, line);
	}

	private static void say(ServerPlayer player, int line) {
		player.sendSystemMessage(Component.literal("<").append(Component.literal("Kenny").withStyle(NAME)).append("> " + LINES[line]));
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_AMBIENT, SoundSource.PLAYERS, 0.5f, 1.6f);
	}

	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		if (original.getPersistentData().getBooleanOr(SEEN_KEY, false))
			event.getEntity().getPersistentData().putBoolean(SEEN_KEY, true);
	}

	@SubscribeEvent
	public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		TALKING.remove(event.getEntity().getUUID());
	}
}
