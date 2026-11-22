package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FenceBlock;

public class ZephyraBlueFenceBlock extends FenceBlock {
	public ZephyraBlueFenceBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(4f, 6f).ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}