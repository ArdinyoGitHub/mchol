package net.mcreator.mchol.music;

import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.sounds.Music;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.Minecraft;

import net.mcreator.mchol.init.McholModSounds;
import net.mcreator.mchol.McholMod;

/**
 * Zephyr Paradise music through the vanilla music manager: only one copy ever plays, it repeats after a short pause
 * and stops when the player leaves the dimension.
 * The MCreator procedure that runs on entering the dimension also starts the track (twice, at the world spawn position);
 * those copies are cancelled here so they cannot pile up on top of each other.
 */
@EventBusSubscriber(Dist.CLIENT)
public class ZephyrMusic {
	private static final ResourceKey<Level> ZEPHYR_PARADISE = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(McholMod.MODID, "zephyr_paradise"));
	private static final Identifier TRACK = Identifier.fromNamespaceAndPath(McholMod.MODID, "zephyrtobacco");
	/** Pause between two plays: 1 to 10 seconds. Replaces the vanilla music right away when entering the dimension. */
	private static final Music MUSIC = new Music(McholModSounds.ZEPHYRTOBACCO, 20, 200, true);

	private static boolean inZephyr(Minecraft mc) {
		return mc.level != null && mc.level.dimension() == ZEPHYR_PARADISE;
	}

	@SubscribeEvent
	public static void onSelectMusic(SelectMusicEvent event) {
		if (inZephyr(Minecraft.getInstance()))
			event.overrideMusic(MUSIC);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		// the vanilla music of other dimensions does not replace a running track, so stop it ourselves
		if (!inZephyr(mc))
			mc.getMusicManager().stopPlaying(MUSIC);
	}

	@SubscribeEvent
	public static void onPlaySound(PlaySoundEvent event) {
		SoundInstance sound = event.getSound();
		// the music manager plays without attenuation; anything else with this track comes from the old procedure
		if (sound != null && TRACK.equals(sound.getIdentifier()) && sound.getAttenuation() != SoundInstance.Attenuation.NONE)
			event.setSound(null);
	}
}
