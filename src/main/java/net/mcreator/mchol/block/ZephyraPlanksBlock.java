package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class ZephyraPlanksBlock extends Block {
	public ZephyraPlanksBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(6f, 7.2246740558f).ignitedByLava().instrument(NoteBlockInstrument.BASS));
	}
}