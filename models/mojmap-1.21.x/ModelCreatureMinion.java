// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ModelCreatureMinion<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "modelcreatureminion"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart arm_left;
	private final ModelPart leg_left;
	private final ModelPart arm_right;
	private final ModelPart leg_right;

	public ModelCreatureMinion(ModelPart root) {
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.arm_left = root.getChild("arm_left");
		this.leg_left = root.getChild("leg_left");
		this.arm_right = root.getChild("arm_right");
		this.leg_right = root.getChild("leg_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -6.0F, -2.5F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 0).addBox(-4.5F, -3.0F, -5.0F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.5F, -10.0F, -5.5F, 11.0F, 7.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(-4.5F, -13.0F, -4.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(22, 18).addBox(-3.5F, -15.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(90, 18).addBox(-2.0F, -16.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition antenna_tip_r1 = head.addOrReplaceChild("antenna_tip_r1", CubeListBuilder.create().texOffs(38, 29).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, 0.5F, 0.4363F, 0.0F, 0.0F));

		PartDefinition antenna_r1 = head.addOrReplaceChild("antenna_r1", CubeListBuilder.create().texOffs(14, 29).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 0.5F, -0.3491F, 0.0F, 0.0F));

		PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left", CubeListBuilder.create(), PartPose.offset(3.5F, 13.0F, 0.0F));

		PartDefinition claw_left_r1 = arm_left.addOrReplaceChild("claw_left_r1", CubeListBuilder.create().texOffs(18, 29).addBox(0.0F, 5.5F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(74, 18).addBox(0.0F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, -0.4363F));

		PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(50, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(106, 18).addBox(-1.5F, 5.0F, -2.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.0F, 0.0F));

		PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right", CubeListBuilder.create(), PartPose.offset(-3.5F, 13.0F, 0.0F));

		PartDefinition claw_right_r1 = arm_right.addOrReplaceChild("claw_right_r1", CubeListBuilder.create().texOffs(28, 29).addBox(-2.0F, 5.5F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(82, 18).addBox(-2.0F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.4363F));

		PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(62, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-1.5F, 5.0F, -2.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI) + Mth.sin(ageInTicks * 0.6F) * 0.08F;
		this.leg_right.xRot = Mth.cos(limbSwing * 1.2F) * 1.4F * limbSwingAmount;
		this.leg_left.xRot = Mth.cos(limbSwing * 1.2F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.arm_right.xRot = Mth.cos(limbSwing * 1.2F + (float) Math.PI) * 1.2F * limbSwingAmount;
		this.arm_left.xRot = Mth.cos(limbSwing * 1.2F) * 1.2F * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		arm_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		arm_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}