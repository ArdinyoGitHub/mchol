package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FenceGateBlock;

public class ZephyraBlueFenceGateBlock extends FenceGateBlock {
	public ZephyraBlueFenceGateBlock(BlockBehaviour.Properties properties) {
		super(WoodType.OAK, properties.sound(SoundType.WOOD).strength(4f, 6f).ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}