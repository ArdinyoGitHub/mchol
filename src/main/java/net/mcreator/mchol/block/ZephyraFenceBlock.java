package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FenceBlock;

public class ZephyraFenceBlock extends FenceBlock {
	public ZephyraFenceBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(6f, 9f).ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}