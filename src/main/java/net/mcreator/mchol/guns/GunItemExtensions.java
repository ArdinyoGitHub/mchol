package net.mcreator.mchol.guns;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

import javax.annotation.Nullable;

/** Gun poses: aiming with both arms in third person, recoil and reload motion in first person. */
public class GunItemExtensions implements IClientItemExtensions {
	@Override
	@Nullable
	public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
		return hand == InteractionHand.MAIN_HAND ? HumanoidModel.ArmPose.CROSSBOW_HOLD : null;
	}

	@Override
	public boolean applyForgeHandTransform(PoseStack poseStack, PlayerRenderState playerRenderState, HumanoidArm arm, ItemStack stack, float partialTick, float equipProcess, float swingProcess) {
		GunSpec spec = GunSpec.get(stack);
		// first person hands always belong to the local player
		LocalPlayer player = Minecraft.getInstance().player;
		if (spec == null || player == null)
			return false;
		int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
		// same base position as vanilla, but without the swing animation
		poseStack.translate(invert * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
		if (arm == player.getMainArm()) {
			// recoil: gun is pushed back towards the camera and the barrel kicks up
			float kick = GunClientEvents.recoilAmount() * spec.recoil();
			poseStack.translate(0, 0, kick * 0.03F);
			poseStack.rotate(Axis.XP.rotationDegrees(kick * 2.5F));
		}
		// reload: the reload cooldown is synced by vanilla, so its progress drives the animation
		float reload = player.getCooldowns().getCooldownPercent(stack, partialTick);
		if (reload > 0) {
			float dip = Mth.sin((1 - reload) * (float) Math.PI);
			poseStack.translate(0, -0.3F * dip, 0.1F * dip);
			poseStack.rotate(Axis.XP.rotationDegrees(-25F * dip));
			poseStack.rotate(Axis.ZP.rotationDegrees(invert * 35F * dip));
		}
		return true;
	}
}
