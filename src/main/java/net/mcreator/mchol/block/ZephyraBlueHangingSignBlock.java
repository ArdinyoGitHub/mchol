package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.CeilingHangingSignBlock;

import net.mcreator.mchol.init.McholModWoodTypes;

public class ZephyraBlueHangingSignBlock extends CeilingHangingSignBlock {
	public ZephyraBlueHangingSignBlock(BlockBehaviour.Properties properties) {
		super(McholModWoodTypes.ZEPHYRA_BLUE_HANGING_SIGN_WOOD_TYPE, properties.sound(SoundType.HANGING_SIGN).strength(2f).noCollision().ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}