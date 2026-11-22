package net.mcreator.mchol.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ColorParticleOption;

public class ZephyrLeavesBlock extends UntintedParticleLeavesBlock {
	public ZephyrLeavesBlock(BlockBehaviour.Properties properties) {
		super(0f, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, -3538866), AmbientLeavesBlockSoundPlayer.noAmbientSound(),
				properties.sound(SoundType.GRAVEL).strength(1f, 10f).noOcclusion().ignitedByLava().isSuffocating((bs, br, bp) -> false).isViewBlocking((bs, br, bp, aabb) -> false));
	}
}