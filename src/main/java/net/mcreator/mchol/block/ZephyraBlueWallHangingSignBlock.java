package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.SoundType;

import net.mcreator.mchol.init.McholModWoodTypes;
import net.mcreator.mchol.init.McholModBlocks;

public class ZephyraBlueWallHangingSignBlock extends WallHangingSignBlock {
	public ZephyraBlueWallHangingSignBlock(BlockBehaviour.Properties properties) {
		super(McholModWoodTypes.ZEPHYRA_BLUE_HANGING_SIGN_WOOD_TYPE, properties.sound(SoundType.HANGING_SIGN).strength(2f).noCollision().ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn()
				.overrideLootTable(McholModBlocks.ZEPHYRA_BLUE_HANGING_SIGN.get().getLootTable()).overrideDescription(McholModBlocks.ZEPHYRA_BLUE_HANGING_SIGN.get().getDescriptionId()));
	}
}