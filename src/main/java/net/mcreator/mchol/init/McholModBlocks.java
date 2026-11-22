/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.Block;

import net.mcreator.mchol.block.*;
import net.mcreator.mchol.McholMod;

import java.util.function.Function;

@EventBusSubscriber
public class McholModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(McholMod.MODID);
	public static final DeferredBlock<Block> ZEPHYR_MAIN;
	public static final DeferredBlock<Block> ZEPHYR_LEAVES;
	public static final DeferredBlock<Block> BLUEGRASS;
	public static final DeferredBlock<Block> SKYFLOWER;
	public static final DeferredBlock<Block> FLAMEFLOWER;
	public static final DeferredBlock<Block> BH_9_K;
	public static final DeferredBlock<Block> CORRUPTEDDIRT;
	public static final DeferredBlock<Block> ZEPHYR_STONE;
	public static final DeferredBlock<Block> ZEPHYR_COBBLE_STONE;
	public static final DeferredBlock<Block> ZEPHYRA_WOOD;
	public static final DeferredBlock<Block> ZEPHYRA_LOG;
	public static final DeferredBlock<Block> ZEPHYRA_PLANKS;
	public static final DeferredBlock<Block> ZEPHYRA_LEAVES;
	public static final DeferredBlock<Block> ZEPHYRA_STAIRS;
	public static final DeferredBlock<Block> ZEPHYRA_SLAB;
	public static final DeferredBlock<Block> ZEPHYRA_FENCE;
	public static final DeferredBlock<Block> ZEPHYRA_FENCE_GATE;
	public static final DeferredBlock<Block> ZEPHYRA_PRESSURE_PLATE;
	public static final DeferredBlock<Block> ZEPHYRA_BUTTON;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_LOG;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_WOOD;
	public static final DeferredBlock<Block> STRIPPED_ZEPHYRA_BLUE_LOG;
	public static final DeferredBlock<Block> STRIPPED_ZEPHYRA_BLUE_WOOD;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_PLANKS;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_LEAVES;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_STAIRS;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_SLAB;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_FENCE;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_FENCE_GATE;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_DOOR;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_TRAPDOOR;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_PRESSURE_PLATE;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_BUTTON;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_SIGN;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_WALL_SIGN;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_HANGING_SIGN;
	public static final DeferredBlock<Block> ZEPHYRA_BLUE_WALL_HANGING_SIGN;
	static {
		ZEPHYR_MAIN = register("zephyr_main", ZephyrMainBlock::new);
		ZEPHYR_LEAVES = register("zephyr_leaves", ZephyrLeavesBlock::new);
		BLUEGRASS = register("bluegrass", BluegrassBlock::new);
		SKYFLOWER = register("skyflower", SkyflowerBlock::new);
		FLAMEFLOWER = register("flameflower", FlameflowerBlock::new);
		BH_9_K = register("bh_9_k", BH9KBlock::new);
		CORRUPTEDDIRT = register("corrupteddirt", CorrupteddirtBlock::new);
		ZEPHYR_STONE = register("zephyr_stone", ZephyrStoneBlock::new);
		ZEPHYR_COBBLE_STONE = register("zephyr_cobble_stone", ZephyrCobbleStoneBlock::new);
		ZEPHYRA_WOOD = register("zephyra_wood", ZephyraWoodBlock::new);
		ZEPHYRA_LOG = register("zephyra_log", ZephyraLogBlock::new);
		ZEPHYRA_PLANKS = register("zephyra_planks", ZephyraPlanksBlock::new);
		ZEPHYRA_LEAVES = register("zephyra_leaves", ZephyraLeavesBlock::new);
		ZEPHYRA_STAIRS = register("zephyra_stairs", ZephyraStairsBlock::new);
		ZEPHYRA_SLAB = register("zephyra_slab", ZephyraSlabBlock::new);
		ZEPHYRA_FENCE = register("zephyra_fence", ZephyraFenceBlock::new);
		ZEPHYRA_FENCE_GATE = register("zephyra_fence_gate", ZephyraFenceGateBlock::new);
		ZEPHYRA_PRESSURE_PLATE = register("zephyra_pressure_plate", ZephyraPressurePlateBlock::new);
		ZEPHYRA_BUTTON = register("zephyra_button", ZephyraButtonBlock::new);
		ZEPHYRA_BLUE_LOG = register("zephyra_blue_log", ZephyraBlueLogBlock::new);
		ZEPHYRA_BLUE_WOOD = register("zephyra_blue_wood", ZephyraBlueWoodBlock::new);
		STRIPPED_ZEPHYRA_BLUE_LOG = register("stripped_zephyra_blue_log", StrippedZephyraBlueLogBlock::new);
		STRIPPED_ZEPHYRA_BLUE_WOOD = register("stripped_zephyra_blue_wood", StrippedZephyraBlueWoodBlock::new);
		ZEPHYRA_BLUE_PLANKS = register("zephyra_blue_planks", ZephyraBluePlanksBlock::new);
		ZEPHYRA_BLUE_LEAVES = register("zephyra_blue_leaves", ZephyraBlueLeavesBlock::new);
		ZEPHYRA_BLUE_STAIRS = register("zephyra_blue_stairs", ZephyraBlueStairsBlock::new);
		ZEPHYRA_BLUE_SLAB = register("zephyra_blue_slab", ZephyraBlueSlabBlock::new);
		ZEPHYRA_BLUE_FENCE = register("zephyra_blue_fence", ZephyraBlueFenceBlock::new);
		ZEPHYRA_BLUE_FENCE_GATE = register("zephyra_blue_fence_gate", ZephyraBlueFenceGateBlock::new);
		ZEPHYRA_BLUE_DOOR = register("zephyra_blue_door", ZephyraBlueDoorBlock::new);
		ZEPHYRA_BLUE_TRAPDOOR = register("zephyra_blue_trapdoor", ZephyraBlueTrapdoorBlock::new);
		ZEPHYRA_BLUE_PRESSURE_PLATE = register("zephyra_blue_pressure_plate", ZephyraBluePressurePlateBlock::new);
		ZEPHYRA_BLUE_BUTTON = register("zephyra_blue_button", ZephyraBlueButtonBlock::new);
		ZEPHYRA_BLUE_SIGN = register("zephyra_blue_sign", ZephyraBlueSignBlock::new);
		ZEPHYRA_BLUE_WALL_SIGN = register("zephyra_blue_wall_sign", ZephyraBlueWallSignBlock::new);
		ZEPHYRA_BLUE_HANGING_SIGN = register("zephyra_blue_hanging_sign", ZephyraBlueHangingSignBlock::new);
		ZEPHYRA_BLUE_WALL_HANGING_SIGN = register("zephyra_blue_wall_hanging_sign", ZephyraBlueWallHangingSignBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}

	@SubscribeEvent
	public static void registerSigns(BlockEntityTypeAddBlocksEvent event) {
		event.modify(BlockEntityTypes.SIGN, ZEPHYRA_BLUE_SIGN.get(), ZEPHYRA_BLUE_WALL_SIGN.get());
		event.modify(BlockEntityTypes.HANGING_SIGN, ZEPHYRA_BLUE_HANGING_SIGN.get(), ZEPHYRA_BLUE_WALL_HANGING_SIGN.get());
	}
}