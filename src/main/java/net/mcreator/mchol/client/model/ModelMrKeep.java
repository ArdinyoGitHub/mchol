package net.mcreator.mchol.client.model;

import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelMrKeep extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("mchol", "model_mr_keep"), "main");
	public final ModelPart body;
	public final ModelPart head;
	public final ModelPart leg_left;
	public final ModelPart arm_left;
	public final ModelPart leg_right;
	public final ModelPart arm_right;

	public ModelMrKeep(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.leg_left = root.getChild("leg_left");
		this.arm_left = root.getChild("arm_left");
		this.leg_right = root.getChild("leg_right");
		this.arm_right = root.getChild("arm_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -1.5F, -9.0F, 18.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(72, 72).addBox(-7.5F, 1.0F, -11.5F, 15.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(64, 0).mirror()
						.addBox(-8.5F, -8.0F, -6.5F, 17.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(30, 58).addBox(4.5F, -9.5F, -4.0F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(62, 58)
						.addBox(-11.5F, -9.5F, -4.0F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 25).addBox(-7.5F, -4.5F, -6.5F, 15.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 42).addBox(-6.0F, -10.0F, -5.5F, 12.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(26, 72).mirror()
						.addBox(-4.5F, -13.5F, -4.0F, 9.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(64, 85).addBox(-4.0F, -4.5F, -8.0F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(84, 85)
						.addBox(-1.0F, -7.0F, -6.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 85).addBox(-5.5F, -9.5F, 4.5F, 11.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -2.0F, -0.5F));
		PartDefinition hair_right_r1 = head.addOrReplaceChild("hair_right_r1", CubeListBuilder.create().texOffs(66, 72).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.5F, -9.0F, 3.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition hair_left_r1 = head.addOrReplaceChild("hair_left_r1", CubeListBuilder.create().texOffs(60, 72).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.5F, -9.0F, 3.0F, 0.0F, 0.0F, -0.5236F));
		PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left",
				CubeListBuilder.create().texOffs(94, 58).addBox(-3.0F, 0.0F, -3.5F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(24, 85).addBox(-2.5F, 5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(4.5F, 14.0F, -0.5F));
		PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(54, 25).addBox(-3.0F, -0.5F, -4.0F, 7.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(44, 42)
				.addBox(-2.5F, 8.0F, -3.5F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(96, 42).addBox(-2.5F, 15.5F, -4.5F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -1.0F, -0.5F));
		PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right",
				CubeListBuilder.create().texOffs(0, 72).addBox(-3.0F, 0.0F, -3.5F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(44, 85).addBox(-2.5F, 5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.5F, 14.0F, -0.5F));
		PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(84, 25).addBox(-4.0F, -0.5F, -4.0F, 7.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(70, 42)
				.addBox(-3.5F, 8.0F, -3.5F, 6.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 58).addBox(-4.5F, 15.5F, -4.5F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.0F, -1.0F, -0.5F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.leg_right.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg_left.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.arm_left.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.arm_right.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
	}

}