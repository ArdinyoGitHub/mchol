package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class ZephyraBluePlanksBlock extends Block {
	public ZephyraBluePlanksBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.WOOD).strength(4f, 5.2233033798f).ignitedByLava().instrument(NoteBlockInstrument.BASS));
	}
}