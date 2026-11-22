/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.mchol.McholMod;

@EventBusSubscriber
public class McholModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, McholMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HIGH_ON_LIFE = REGISTRY.register("high_on_life",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.mchol.high_on_life")).icon(() -> new ItemStack(McholModItems.KENNY.get())).displayItems((parameters, tabData) -> {
				tabData.accept(McholModItems.KENNY.get());
				tabData.accept(McholModItems.GUS.get());
				tabData.accept(McholModItems.SWEEZY.get());
				tabData.accept(McholModItems.CREATUREE.get());
				tabData.accept(McholModBlocks.ZEPHYR_MAIN.get().asItem());
				tabData.accept(McholModBlocks.ZEPHYR_LEAVES.get().asItem());
				tabData.accept(McholModBlocks.BH_9_K.get().asItem());
				tabData.accept(McholModBlocks.CORRUPTEDDIRT.get().asItem());
				tabData.accept(McholModItems.GOOP_SUCK_GUS.get());
				tabData.accept(McholModItems.MULTI_GLOB_KENNY.get());
				tabData.accept(McholModItems.PORTAL_SWEEZY.get());
				tabData.accept(McholModItems.BOUNTY_HUNTER_HELMET.get());
				tabData.accept(McholModItems.BOUNTY_HUNTER_CHESTPLATE.get());
				tabData.accept(McholModItems.BOUNTY_HUNTER_LEGGINGS.get());
				tabData.accept(McholModItems.BOUNTY_HUNTER_BOOTS.get());
				tabData.accept(McholModBlocks.ZEPHYR_STONE.get().asItem());
				tabData.accept(McholModBlocks.ZEPHYR_COBBLE_STONE.get().asItem());
				tabData.accept(McholModItems.KRUBIS_SPAWN_EGG.get());
				tabData.accept(McholModItems.DOUGLAS_SPAWN_EGG.get());
				tabData.accept(McholModItems.GENE_SPAWN_EGG.get());
				tabData.accept(McholModItems.MR_KEEPS_SPAWN_EGG.get());
				tabData.accept(McholModItems.MULTI_GLOB_UNIT.get());
				tabData.accept(McholModItems.GOOP_SUCK_UNIT.get());
				tabData.accept(McholModItems.PORTAL_UNIT.get());
				tabData.accept(McholModItems.CREATURE_MINION_SPAWN_EGG.get());
				tabData.accept(McholModItems.LEZDUIT.get());
			}).withSearchBar().build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(McholModItems.KENNY.get());
			tabData.accept(McholModItems.GUS.get());
			tabData.accept(McholModItems.SWEEZY.get());
			tabData.accept(McholModItems.CREATUREE.get());
			tabData.accept(McholModItems.GOOP_SUCK_GUS.get());
			tabData.accept(McholModItems.MULTI_GLOB_KENNY.get());
			tabData.accept(McholModItems.PORTAL_SWEEZY.get());
			tabData.accept(McholModItems.BOUNTY_HUNTER_HELMET.get());
			tabData.accept(McholModItems.BOUNTY_HUNTER_CHESTPLATE.get());
			tabData.accept(McholModItems.BOUNTY_HUNTER_LEGGINGS.get());
			tabData.accept(McholModItems.BOUNTY_HUNTER_BOOTS.get());
			tabData.accept(McholModItems.LEZDUIT.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(McholModBlocks.ZEPHYR_MAIN.get().asItem());
			tabData.accept(McholModBlocks.BLUEGRASS.get().asItem());
			tabData.accept(McholModBlocks.SKYFLOWER.get().asItem());
			tabData.accept(McholModBlocks.FLAMEFLOWER.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_LEAVES.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_LEAVES.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(McholModItems.MERK_SPAWN_EGG.get());
			tabData.accept(McholModItems.BLUE_MYTE_SPAWN_EGG.get());
			tabData.accept(McholModItems.TESTENTITY_SPAWN_EGG.get());
			tabData.accept(McholModItems.KRUBIS_SPAWN_EGG.get());
			tabData.accept(McholModItems.DOUGLAS_SPAWN_EGG.get());
			tabData.accept(McholModItems.GENE_SPAWN_EGG.get());
			tabData.accept(McholModItems.MR_KEEPS_SPAWN_EGG.get());
			tabData.accept(McholModItems.CREATURE_MINION_SPAWN_EGG.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			tabData.accept(McholModBlocks.ZEPHYRA_WOOD.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_LOG.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_PLANKS.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_STAIRS.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_SLAB.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_FENCE.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_FENCE_GATE.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_PRESSURE_PLATE.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BUTTON.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_LOG.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_WOOD.get().asItem());
			tabData.accept(McholModBlocks.STRIPPED_ZEPHYRA_BLUE_LOG.get().asItem());
			tabData.accept(McholModBlocks.STRIPPED_ZEPHYRA_BLUE_WOOD.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_PLANKS.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_STAIRS.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_SLAB.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_FENCE.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_FENCE_GATE.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_DOOR.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_TRAPDOOR.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_PRESSURE_PLATE.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_BUTTON.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_SIGN.get().asItem());
			tabData.accept(McholModBlocks.ZEPHYRA_BLUE_HANGING_SIGN.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(McholModItems.ZEPHYRA_BLUE_BOAT.get());
			tabData.accept(McholModItems.ZEPHYRA_BLUE_CHEST_BOAT.get());
		}
	}
}