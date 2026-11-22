/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.mchol.McholMod;

public class McholModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, McholMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> ZEPHYRTOBACCO = REGISTRY.register("zephyrtobacco", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "zephyrtobacco")));
	public static final DeferredHolder<SoundEvent, SoundEvent> GUSHOT = REGISTRY.register("gushot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "gushot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> CREATURESHOT = REGISTRY.register("creatureshot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "creatureshot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> KENNYSHOT = REGISTRY.register("kennyshot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "kennyshot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SWEEZYSHOT = REGISTRY.register("sweezyshot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "sweezyshot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> GATLUS = REGISTRY.register("gatlus", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "gatlus")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SWEEZSYSHOT = REGISTRY.register("sweezsyshot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "sweezsyshot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ZEPHYRS = REGISTRY.register("zephyrs", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "zephyrs")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PORTTERENNE = REGISTRY.register("portterenne", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mchol", "portterenne")));
}