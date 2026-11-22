// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ModelDouglas<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "modeldouglas"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart arm_left;
	private final ModelPart leg_left;
	private final ModelPart arm_right;
	private final ModelPart leg_right;

	public ModelDouglas(ModelPart root) {
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

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(64, 60).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 46).addBox(-5.0F, -12.0F, -3.0F, 10.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 46).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -15.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-5.0F, -16.0F, -5.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(56, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(92, 60).addBox(-2.0F, 13.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 46).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -2.0F, 0.0F));

		PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(88, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 60).addBox(-2.0F, 11.0F, -3.5F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 9.0F, 0.0F));

		PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(72, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(108, 60).addBox(-2.0F, 13.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 60).addBox(-4.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -2.0F, 0.0F));

		PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(104, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 60).addBox(-2.0F, 11.0F, -3.5F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.leg_right.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg_left.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.arm_left.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.arm_right.xRot = -1.4F + headPitch / (180F / (float) Math.PI);
		this.arm_right.yRot = netHeadYaw / (180F / (float) Math.PI) * 0.5F;
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