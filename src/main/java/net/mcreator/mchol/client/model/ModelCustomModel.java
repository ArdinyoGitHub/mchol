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

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelCustomModel extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("mchol", "model_custom_model"), "main");
	public final ModelPart sagkol;
	public final ModelPart solkol;
	public final ModelPart sagbacak;
	public final ModelPart solbacak;
	public final ModelPart kafa;
	public final ModelPart vucut;

	public ModelCustomModel(ModelPart root) {
		super(root);
		this.sagkol = root.getChild("sagkol");
		this.solkol = root.getChild("solkol");
		this.sagbacak = root.getChild("sagbacak");
		this.solbacak = root.getChild("solbacak");
		this.kafa = root.getChild("kafa");
		this.vucut = root.getChild("vucut");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition sagkol = partdefinition.addOrReplaceChild("sagkol", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 12.0F, 0.0F));
		PartDefinition solkol = partdefinition.addOrReplaceChild("solkol", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 12.0F, 0.0F));
		PartDefinition sagbacak = partdefinition.addOrReplaceChild("sagbacak", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 24.0F, 0.0F));
		PartDefinition solbacak = partdefinition.addOrReplaceChild("solbacak", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 24.0F, 0.0F));
		PartDefinition kafa = partdefinition.addOrReplaceChild("kafa", CubeListBuilder.create().texOffs(-5, -3).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, 0.0F));
		PartDefinition vucut = partdefinition.addOrReplaceChild("vucut", CubeListBuilder.create().texOffs(-6, 0).addBox(1.0F, -11.0F, -1.0F, 6.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 12.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.kafa.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.kafa.xRot = headPitch / (180F / (float) Math.PI);
		this.solkol.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.solbacak.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.sagkol.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.sagbacak.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}