/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.component.Compostable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

@EventBusSubscriber
public class McholModCompostables {
	@SubscribeEvent
	public static void modifyItemComponents(ModifyDefaultComponentsEvent event) {
		event.modify(McholModBlocks.ZEPHYRA_BLUE_LEAVES.get().asItem(),
				(builder, _, _) -> builder.set(DataComponents.COMPOSTABLE, new Compostable(ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, Identifier.fromNamespaceAndPath("mchol", "compostable/zephyra_blue_leaves_composting")))));
	}
}