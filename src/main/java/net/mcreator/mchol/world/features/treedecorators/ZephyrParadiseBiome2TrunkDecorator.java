package net.mcreator.mchol.world.features.treedecorators;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import com.mojang.serialization.MapCodec;

@EventBusSubscriber
public class ZephyrParadiseBiome2TrunkDecorator extends TrunkVineDecorator {
	public static MapCodec<ZephyrParadiseBiome2TrunkDecorator> CODEC = MapCodec.unit(ZephyrParadiseBiome2TrunkDecorator::new);
	public static TreeDecoratorType<?> DECORATOR_TYPE = new TreeDecoratorType<>(CODEC);

	@SubscribeEvent
	public static void registerTreeDecorator(RegisterEvent event) {
		event.register(Registries.TREE_DECORATOR_TYPE, Identifier.parse("mchol:zephyr_paradise_biome_2_tree_trunk_decorator"), () -> DECORATOR_TYPE);
	}

	@Override
	protected TreeDecoratorType<?> type() {
		return DECORATOR_TYPE;
	}

	@Override
	public void place(TreeDecorator.Context context) {
		context.logs().forEach(blockpos -> {
			if (context.random().nextInt(3) > 0) {
				BlockPos pos = blockpos.west();
				if (context.isAir(pos)) {
					context.setBlock(pos, oriented(Blocks.TWISTING_VINES.defaultBlockState(), Direction.EAST));
				}
			}
			if (context.random().nextInt(3) > 0) {
				BlockPos pos = blockpos.east();
				if (context.isAir(pos)) {
					context.setBlock(pos, oriented(Blocks.TWISTING_VINES.defaultBlockState(), Direction.WEST));
				}
			}
			if (context.random().nextInt(3) > 0) {
				BlockPos pos = blockpos.north();
				if (context.isAir(pos)) {
					context.setBlock(pos, oriented(Blocks.TWISTING_VINES.defaultBlockState(), Direction.SOUTH));
				}
			}
			if (context.random().nextInt(3) > 0) {
				BlockPos pos = blockpos.south();
				if (context.isAir(pos)) {
					context.setBlock(pos, oriented(Blocks.TWISTING_VINES.defaultBlockState(), Direction.NORTH));
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	private static BlockState oriented(BlockState blockstate, Direction direction) {
		return switch (direction) {
			case SOUTH -> blockstate.rotate(Rotation.CLOCKWISE_180);
			case EAST -> blockstate.rotate(Rotation.CLOCKWISE_90);
			case WEST -> blockstate.rotate(Rotation.COUNTERCLOCKWISE_90);
			default -> blockstate;
		};
	}
}