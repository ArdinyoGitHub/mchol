package net.mcreator.mchol.block;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.PressurePlateBlock;

public class ZephyraBluePressurePlateBlock extends PressurePlateBlock {
	public ZephyraBluePressurePlateBlock(BlockBehaviour.Properties properties) {
		super(BlockSetType.OAK, properties.sound(SoundType.WOOD).strength(1f).noCollision().pushReaction(PushReaction.POPPED).ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}