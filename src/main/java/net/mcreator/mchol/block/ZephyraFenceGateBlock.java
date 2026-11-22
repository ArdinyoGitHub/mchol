package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FenceGateBlock;

public class ZephyraFenceGateBlock extends FenceGateBlock {
	public ZephyraFenceGateBlock(BlockBehaviour.Properties properties) {
		super(WoodType.OAK, properties.sound(SoundType.WOOD).strength(6f, 9f).ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}