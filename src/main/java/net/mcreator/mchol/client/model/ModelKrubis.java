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
public class ModelKrubis extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("mchol", "model_krubis"), "main");
	public final ModelPart body;
	public final ModelPart head;
	public final ModelPart arm_left;
	public final ModelPart arm_right;
	public final ModelPart leg_left;
	public final ModelPart leg_right;

	public ModelKrubis(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.arm_left = root.getChild("arm_left");
		this.arm_right = root.getChild("arm_right");
		this.leg_left = root.getChild("leg_left");
		this.leg_right = root.getChild("leg_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(44, 40).addBox(-4.5F, -1.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(116, 48).addBox(-1.5F, -0.5F, -3.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 0)
						.addBox(-4.0F, -10.0F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-5.0F, -10.0F, -3.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(94, 20)
						.addBox(-4.0F, -12.0F, -3.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 4.0F, 0.0F));
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(88, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(12, 48).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 20)
						.addBox(-4.0F, -9.0F, -3.5F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(58, 48).addBox(-3.5F, -7.0F, -4.5F, 7.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(92, 31)
						.addBox(-3.0F, -11.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(72, 40).addBox(-2.5F, -13.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(30, 48)
						.addBox(-2.0F, -15.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(104, 48).addBox(-1.5F, -17.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 55)
						.addBox(-1.0F, -19.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));
		PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left",
				CubeListBuilder.create().texOffs(32, 40).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 48).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 40)
						.addBox(-2.5F, 9.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(108, 40).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(46, 48)
						.addBox(-1.5F, 15.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(8, 55).addBox(-1.0F, 18.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(16, 55)
						.addBox(-0.5F, 20.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.5F, -4.0F, 0.0F));
		PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right",
				CubeListBuilder.create().texOffs(30, 20).addBox(-4.0F, -4.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(20, 40).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(80, 31)
						.addBox(-1.5F, 5.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(92, 40).addBox(-2.0F, 11.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.5F, -4.0F, 0.0F));
		PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 31).addBox(-2.5F, 6.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(54, 20)
						.addBox(-2.5F, 10.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(20, 31).addBox(-2.5F, 16.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(76, 48)
						.addBox(-2.5F, 17.0F, -4.5F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.5F, 4.0F, 0.0F));
		PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right",
				CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 31).addBox(-2.5F, 6.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(74, 20)
						.addBox(-2.5F, 10.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(60, 31).addBox(-2.5F, 16.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(90, 48)
						.addBox(-2.5F, 17.0F, -4.5F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.5F, 4.0F, 0.0F));
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
		this.arm_left.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.arm_right.xRot = -1.4F + headPitch / (180F / (float) Math.PI);
		this.arm_right.yRot = netHeadYaw / (180F / (float) Math.PI) * 0.5F;
	}

}