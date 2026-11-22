package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.SoundType;

import net.mcreator.mchol.init.McholModWoodTypes;

public class ZephyraBlueSignBlock extends StandingSignBlock {
	public ZephyraBlueSignBlock(BlockBehaviour.Properties properties) {
		super(McholModWoodTypes.ZEPHYRA_BLUE_SIGN_WOOD_TYPE, properties.sound(SoundType.WOOD).strength(2f).noCollision().ignitedByLava().instrument(NoteBlockInstrument.BASS).forceSolidOn());
	}
}