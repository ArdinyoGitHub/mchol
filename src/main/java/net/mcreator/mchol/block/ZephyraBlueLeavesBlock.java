package net.mcreator.mchol.block;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ColorParticleOption;

public class ZephyraBlueLeavesBlock extends UntintedParticleLeavesBlock {
	public ZephyraBlueLeavesBlock(BlockBehaviour.Properties properties) {
		super(0f, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, -13019218), AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties.sound(SoundType.GRASS).strength(0.4f).noOcclusion().pushReaction(PushReaction.POPPED)
				.isRedstoneConductor((bs, br, bp) -> false).ignitedByLava().isSuffocating((bs, br, bp) -> false).isViewBlocking((bs, br, bp, aabb) -> false));
	}
}