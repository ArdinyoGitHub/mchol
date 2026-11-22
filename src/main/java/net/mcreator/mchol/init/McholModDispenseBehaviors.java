/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;

@EventBusSubscriber
public class McholModDispenseBehaviors {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			DispenserBlock.registerBehavior(McholModItems.ZEPHYRA_BLUE_BOAT.get(), new BoatDispenseItemBehavior(McholModEntities.ZEPHYRA_BLUE_BOAT.get()));
			DispenserBlock.registerBehavior(McholModItems.ZEPHYRA_BLUE_CHEST_BOAT.get(), new BoatDispenseItemBehavior(McholModEntities.ZEPHYRA_BLUE_CHEST_BOAT.get()));
		});
	}
}