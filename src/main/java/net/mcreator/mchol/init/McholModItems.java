/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.*;
import net.minecraft.core.Direction;

import net.mcreator.mchol.item.*;
import net.mcreator.mchol.McholMod;

import java.util.function.Function;

public class McholModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(McholMod.MODID);
	public static final DeferredItem<Item> KENNY;
	public static final DeferredItem<Item> GUS;
	public static final DeferredItem<Item> SWEEZY;
	public static final DeferredItem<Item> CREATUREE;
	public static final DeferredItem<Item> ZEPHYR_MAIN;
	public static final DeferredItem<Item> ZEPHYR_LEAVES;
	public static final DeferredItem<Item> BLUEGRASS;
	public static final DeferredItem<Item> SKYFLOWER;
	public static final DeferredItem<Item> FLAMEFLOWER;
	public static final DeferredItem<Item> BH_9_K;
	public static final DeferredItem<Item> CORRUPTEDDIRT;
	public static final DeferredItem<Item> MERK_SPAWN_EGG;
	public static final DeferredItem<Item> GOOP_SUCK_GUS;
	public static final DeferredItem<Item> MULTI_GLOB_KENNY;
	public static final DeferredItem<Item> PORTAL_SWEEZY;
	public static final DeferredItem<Item> BOUNTY_HUNTER_HELMET;
	public static final DeferredItem<Item> BOUNTY_HUNTER_CHESTPLATE;
	public static final DeferredItem<Item> BOUNTY_HUNTER_LEGGINGS;
	public static final DeferredItem<Item> BOUNTY_HUNTER_BOOTS;
	public static final DeferredItem<Item> ZEPHYR_STONE;
	public static final DeferredItem<Item> ZEPHYR_COBBLE_STONE;
	public static final DeferredItem<Item> GATTERALTYPE_3;
	public static final DeferredItem<Item> BLUE_MYTE_SPAWN_EGG;
	public static final DeferredItem<Item> ZEPHYRA_WOOD;
	public static final DeferredItem<Item> ZEPHYRA_LOG;
	public static final DeferredItem<Item> ZEPHYRA_PLANKS;
	public static final DeferredItem<Item> ZEPHYRA_LEAVES;
	public static final DeferredItem<Item> ZEPHYRA_STAIRS;
	public static final DeferredItem<Item> ZEPHYRA_SLAB;
	public static final DeferredItem<Item> ZEPHYRA_FENCE;
	public static final DeferredItem<Item> ZEPHYRA_FENCE_GATE;
	public static final DeferredItem<Item> ZEPHYRA_PRESSURE_PLATE;
	public static final DeferredItem<Item> ZEPHYRA_BUTTON;
	public static final DeferredItem<Item> TESTENTITY_SPAWN_EGG;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_LOG;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_WOOD;
	public static final DeferredItem<Item> STRIPPED_ZEPHYRA_BLUE_LOG;
	public static final DeferredItem<Item> STRIPPED_ZEPHYRA_BLUE_WOOD;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_PLANKS;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_LEAVES;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_STAIRS;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_SLAB;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_FENCE;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_FENCE_GATE;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_DOOR;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_TRAPDOOR;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_PRESSURE_PLATE;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_BUTTON;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_SIGN;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_HANGING_SIGN;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_BOAT;
	public static final DeferredItem<Item> ZEPHYRA_BLUE_CHEST_BOAT;
	public static final DeferredItem<Item> KRUBIS_SPAWN_EGG;
	public static final DeferredItem<Item> DOUGLAS_SPAWN_EGG;
	public static final DeferredItem<Item> GENE_SPAWN_EGG;
	public static final DeferredItem<Item> MR_KEEPS_SPAWN_EGG;
	public static final DeferredItem<Item> MULTI_GLOB_UNIT;
	public static final DeferredItem<Item> GOOP_SUCK_UNIT;
	public static final DeferredItem<Item> PORTAL_UNIT;
	public static final DeferredItem<Item> CREATURE_MINION_SPAWN_EGG;
	public static final DeferredItem<Item> LEZDUIT;
	static {
		KENNY = register("kenny", KennyItem::new);
		GUS = register("gus", GusItem::new);
		SWEEZY = register("sweezy", SweezyItem::new);
		CREATUREE = register("creaturee", CreatureeItem::new);
		ZEPHYR_MAIN = block(McholModBlocks.ZEPHYR_MAIN);
		ZEPHYR_LEAVES = block(McholModBlocks.ZEPHYR_LEAVES);
		BLUEGRASS = block(McholModBlocks.BLUEGRASS);
		SKYFLOWER = block(McholModBlocks.SKYFLOWER);
		FLAMEFLOWER = block(McholModBlocks.FLAMEFLOWER);
		BH_9_K = block(McholModBlocks.BH_9_K);
		CORRUPTEDDIRT = block(McholModBlocks.CORRUPTEDDIRT);
		MERK_SPAWN_EGG = register("merk_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.MERK.get())));
		GOOP_SUCK_GUS = register("goop_suck_gus", GoopSuckGusItem::new);
		MULTI_GLOB_KENNY = register("multi_glob_kenny", MultiGlobKennyItem::new);
		PORTAL_SWEEZY = register("portal_sweezy", PortalSweezyItem::new);
		BOUNTY_HUNTER_HELMET = register("bounty_hunter_helmet", BountyHunterItem.Helmet::new);
		BOUNTY_HUNTER_CHESTPLATE = register("bounty_hunter_chestplate", BountyHunterItem.Chestplate::new);
		BOUNTY_HUNTER_LEGGINGS = register("bounty_hunter_leggings", BountyHunterItem.Leggings::new);
		BOUNTY_HUNTER_BOOTS = register("bounty_hunter_boots", BountyHunterItem.Boots::new);
		ZEPHYR_STONE = block(McholModBlocks.ZEPHYR_STONE);
		ZEPHYR_COBBLE_STONE = block(McholModBlocks.ZEPHYR_COBBLE_STONE);
		GATTERALTYPE_3 = register("gatteraltype_3", Gatteraltype1Item::new);
		BLUE_MYTE_SPAWN_EGG = register("blue_myte_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.BLUE_MYTE.get())));
		ZEPHYRA_WOOD = block(McholModBlocks.ZEPHYRA_WOOD);
		ZEPHYRA_LOG = block(McholModBlocks.ZEPHYRA_LOG);
		ZEPHYRA_PLANKS = block(McholModBlocks.ZEPHYRA_PLANKS);
		ZEPHYRA_LEAVES = block(McholModBlocks.ZEPHYRA_LEAVES);
		ZEPHYRA_STAIRS = block(McholModBlocks.ZEPHYRA_STAIRS);
		ZEPHYRA_SLAB = block(McholModBlocks.ZEPHYRA_SLAB);
		ZEPHYRA_FENCE = block(McholModBlocks.ZEPHYRA_FENCE);
		ZEPHYRA_FENCE_GATE = block(McholModBlocks.ZEPHYRA_FENCE_GATE);
		ZEPHYRA_PRESSURE_PLATE = block(McholModBlocks.ZEPHYRA_PRESSURE_PLATE);
		ZEPHYRA_BUTTON = block(McholModBlocks.ZEPHYRA_BUTTON);
		TESTENTITY_SPAWN_EGG = register("testentity_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.TESTENTITY.get())));
		ZEPHYRA_BLUE_LOG = block(McholModBlocks.ZEPHYRA_BLUE_LOG);
		ZEPHYRA_BLUE_WOOD = block(McholModBlocks.ZEPHYRA_BLUE_WOOD);
		STRIPPED_ZEPHYRA_BLUE_LOG = block(McholModBlocks.STRIPPED_ZEPHYRA_BLUE_LOG);
		STRIPPED_ZEPHYRA_BLUE_WOOD = block(McholModBlocks.STRIPPED_ZEPHYRA_BLUE_WOOD);
		ZEPHYRA_BLUE_PLANKS = block(McholModBlocks.ZEPHYRA_BLUE_PLANKS);
		ZEPHYRA_BLUE_LEAVES = block(McholModBlocks.ZEPHYRA_BLUE_LEAVES);
		ZEPHYRA_BLUE_STAIRS = block(McholModBlocks.ZEPHYRA_BLUE_STAIRS);
		ZEPHYRA_BLUE_SLAB = block(McholModBlocks.ZEPHYRA_BLUE_SLAB);
		ZEPHYRA_BLUE_FENCE = block(McholModBlocks.ZEPHYRA_BLUE_FENCE);
		ZEPHYRA_BLUE_FENCE_GATE = block(McholModBlocks.ZEPHYRA_BLUE_FENCE_GATE);
		ZEPHYRA_BLUE_DOOR = doubleBlock(McholModBlocks.ZEPHYRA_BLUE_DOOR);
		ZEPHYRA_BLUE_TRAPDOOR = block(McholModBlocks.ZEPHYRA_BLUE_TRAPDOOR);
		ZEPHYRA_BLUE_PRESSURE_PLATE = block(McholModBlocks.ZEPHYRA_BLUE_PRESSURE_PLATE);
		ZEPHYRA_BLUE_BUTTON = block(McholModBlocks.ZEPHYRA_BLUE_BUTTON);
		ZEPHYRA_BLUE_SIGN = signBlock(McholModBlocks.ZEPHYRA_BLUE_SIGN, McholModBlocks.ZEPHYRA_BLUE_WALL_SIGN, new Item.Properties().stacksTo(16));
		ZEPHYRA_BLUE_HANGING_SIGN = hangingSignBlock(McholModBlocks.ZEPHYRA_BLUE_HANGING_SIGN, McholModBlocks.ZEPHYRA_BLUE_WALL_HANGING_SIGN, new Item.Properties().stacksTo(16));
		ZEPHYRA_BLUE_BOAT = register("zephyra_blue_boat", properties -> new BoatItem(McholModEntities.ZEPHYRA_BLUE_BOAT.get(), properties.stacksTo(1)));
		ZEPHYRA_BLUE_CHEST_BOAT = register("zephyra_blue_chest_boat", properties -> new BoatItem(McholModEntities.ZEPHYRA_BLUE_CHEST_BOAT.get(), properties.stacksTo(1)));
		KRUBIS_SPAWN_EGG = register("krubis_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.KRUBIS.get())));
		DOUGLAS_SPAWN_EGG = register("douglas_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.DOUGLAS.get())));
		GENE_SPAWN_EGG = register("gene_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.GENE.get())));
		MR_KEEPS_SPAWN_EGG = register("mr_keeps_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.MR_KEEPS.get())));
		MULTI_GLOB_UNIT = register("multi_glob_unit", MultiGlobUnitItem::new);
		GOOP_SUCK_UNIT = register("goop_suck_unit", GoopSuckUnitItem::new);
		PORTAL_UNIT = register("portal_unit", PortalUnitItem::new);
		CREATURE_MINION_SPAWN_EGG = register("creature_minion_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(McholModEntities.CREATURE_MINION.get())));
		LEZDUIT = register("lezduit", LezduitItem::new);
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, Item.Properties::new);
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem(block.get(), prop), () -> properties);
	}

	private static DeferredItem<Item> doubleBlock(DeferredHolder<Block, Block> block) {
		return doubleBlock(block, new Item.Properties());
	}

	private static DeferredItem<Item> doubleBlock(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new DoubleHighBlockItem(block.get(), prop), () -> properties);
	}

	private static DeferredItem<Item> signBlock(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> wallBlock) {
		return signBlock(block, wallBlock, new Item.Properties());
	}

	private static DeferredItem<Item> signBlock(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> wallBlock, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new StandingAndWallBlockItem(block.get(), wallBlock.get(), Direction.DOWN, prop), () -> properties);
	}

	private static DeferredItem<Item> hangingSignBlock(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> wallBlock) {
		return hangingSignBlock(block, wallBlock, new Item.Properties());
	}

	private static DeferredItem<Item> hangingSignBlock(DeferredHolder<Block, Block> block, DeferredHolder<Block, Block> wallBlock, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new HangingSignItem(block.get(), wallBlock.get(), prop), () -> properties);
	}
}