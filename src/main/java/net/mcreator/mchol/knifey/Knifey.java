package net.mcreator.mchol.knifey;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.util.Unit;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.mchol.init.McholModTabs;
import net.mcreator.mchol.init.McholModItems;
import net.mcreator.mchol.McholMod;

/**
 * Knifey, the talking knife. Registered here instead of as an MCreator element so regenerating the workspace never touches it.
 * Fast melee weapon that never breaks.
 */
@EventBusSubscriber
public class Knifey {
	public static final Identifier ID = Identifier.fromNamespaceAndPath(McholMod.MODID, "knifey");
	public static final DeferredHolder<Item, Item> KNIFEY = DeferredHolder.create(Registries.ITEM, ID);

	/** Attack damage 6 (iron sword level), attack speed 2.4 (dagger fast; a sword is 1.6). */
	private static final float DAMAGE_BONUS = 3.0f;
	private static final float SPEED_MODIFIER = -1.6f;

	@SubscribeEvent
	public static void register(RegisterEvent event) {
		event.register(Registries.ITEM, helper -> helper.register(ID,
				new KnifeyBlade(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ID)).sword(ToolMaterial.IRON, DAMAGE_BONUS, SPEED_MODIFIER).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).rarity(Rarity.RARE))));
	}

	@SubscribeEvent
	public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == McholModTabs.HIGH_ON_LIFE.getKey()) {
			// right after the guns instead of at the end of the tab
			event.insertAfter(new ItemStack(McholModItems.CREATUREE.get()), new ItemStack(KNIFEY.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		} else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(KNIFEY.get());
		}
	}

	private static class KnifeyBlade extends Item {
		KnifeyBlade(Item.Properties properties) {
			super(properties);
		}

		/** The generated lang file is rewritten by MCreator, so the English name is a fallback here. */
		@Override
		public Component getName(ItemStack stack) {
			return Component.translatableWithFallback("item.mchol.knifey", "Knifey");
		}
	}
}
