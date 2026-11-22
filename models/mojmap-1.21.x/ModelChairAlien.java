// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ModelChairAlien<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "modelchairalien"), "main");
	private final ModelPart chair;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart arm_left;
	private final ModelPart arm_right;

	public ModelChairAlien(ModelPart root) {
		this.chair = root.getChild("chair");
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.arm_left = root.getChild("arm_left");
		this.arm_right = root.getChild("arm_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition chair = partdefinition.addOrReplaceChild("chair", CubeListBuilder.create().texOffs(0, 25).addBox(-8.0F, -2.0F, -7.0F, 16.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -22.0F, 4.0F, 16.0F, 21.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(78, 68).addBox(-3.0F, 2.0F, 0.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(7.0F, -7.0F, -7.0F, 3.0F, 7.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(104, 0).addBox(6.0F, -21.0F, 1.0F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(52, 78).addBox(3.0F, 2.0F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(-10.0F, -7.0F, -7.0F, 3.0F, 7.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(116, 0).addBox(-9.0F, -21.0F, 1.0F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(84, 78).addBox(-7.0F, 2.0F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(52, 43).addBox(-5.0F, 0.0F, -4.0F, 10.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(60, 25).addBox(-4.5F, -11.0F, -2.5F, 9.0F, 11.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(72, 56).addBox(-2.5F, -15.0F, -3.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(0.5F, -1.0F, -11.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(20, 68).addBox(1.0F, 3.5F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(102, 68).addBox(0.5F, 9.0F, -12.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(26, 43).addBox(-5.5F, -1.0F, -11.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(36, 68).addBox(-5.0F, 3.5F, -12.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 78).addBox(-5.5F, 9.0F, -12.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 1.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(88, 43).addBox(-4.5F, -5.0F, -5.0F, 9.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(90, 25).mirror().addBox(-4.5F, -13.0F, -4.5F, 9.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(52, 68).addBox(-3.5F, -15.5F, -3.0F, 7.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 86).addBox(-4.5F, -6.5F, -6.5F, 9.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 78).addBox(3.5F, -6.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition eyeball_right_r1 = head.addOrReplaceChild("eyeball_right_r1", CubeListBuilder.create().texOffs(0, 68).addBox(-2.5F, -10.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(44, 78).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -12.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition eyeball_left_r1 = head.addOrReplaceChild("eyeball_left_r1", CubeListBuilder.create().texOffs(92, 56).addBox(-2.5F, -10.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(36, 78).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -12.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition arm_left = partdefinition.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(48, 56).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(-1.0F, 6.0F, -8.5F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(68, 78).addBox(-1.5F, 7.5F, -11.5F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 2.0F, 1.0F));

		PartDefinition arm_right = partdefinition.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(60, 56).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(24, 56).addBox(-2.0F, 6.0F, -8.5F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(100, 78).addBox(-2.5F, 7.5F, -11.5F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 2.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		chair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		arm_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		arm_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}