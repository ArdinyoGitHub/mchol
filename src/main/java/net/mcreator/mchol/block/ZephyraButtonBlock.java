package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.ButtonBlock;

public class ZephyraButtonBlock extends ButtonBlock {
	public ZephyraButtonBlock(BlockBehaviour.Properties properties) {
		super(BlockSetType.OAK, 30, properties.sound(SoundType.WOOD).strength(6f, 9f).ignitedByLava().instrument(NoteBlockInstrument.BASS));
	}
}