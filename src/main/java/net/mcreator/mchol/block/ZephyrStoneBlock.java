package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class ZephyrStoneBlock extends Block {
	public ZephyrStoneBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(2f, 10f).instrument(NoteBlockInstrument.BASEDRUM));
	}
}