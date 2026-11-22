// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelmerk<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "merk"), "main");
	private final ModelPart body;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	private final ModelPart leftarm;
	private final ModelPart rightarm;
	private final ModelPart Head;

	public Modelmerk(ModelPart root) {
		this.body = root.getChild("body");
		this.leftleg = root.getChild("leftleg");
		this.rightleg = root.getChild("rightleg");
		this.leftarm = root.getChild("leftarm");
		this.rightarm = root.getChild("rightarm");
		this.Head = root.getChild("Head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -1.0F, 5.0F, 17.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.5F, 10.0F, 1.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition leftleg = partdefinition.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(2.0F, 11.0F, 0.0F));
		PartDefinition cube_r2 = leftleg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(42, 25).addBox(-3.0F, -1.0F, -2.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 1.0F, 1.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition rightleg = partdefinition.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-2.0F, 11.0F, 0.0F));
		PartDefinition cube_r3 = rightleg.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(42, 9).addBox(-3.0F, -4.0F, -2.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 4.0F, 1.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition leftarm = partdefinition.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offset(6.0F, -4.0F, 0.0F));
		PartDefinition cube_r4 = leftarm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(30, 24).mirror().addBox(-1.0F, -1.0F, -1.0F, 3.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 0.0F, -0.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition rightarm = partdefinition.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offset(-6.0F, -4.0F, 0.0F));
		PartDefinition cube_r5 = rightarm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -1.0F, -0.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition head_r1 = Head.addOrReplaceChild("head_r1",
				CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -8.0F, -6.0F, 6.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(40, 0).addBox(-2.0F, -10.0F, -5.0F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(42, 41)
						.addBox(-1.0F, -12.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(10, 47).addBox(-4.0F, -6.0F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(18, 47)
						.addBox(-4.0F, -6.0F, -5.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.5F, -4.0F, -0.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r6 = head_r1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(34, 48).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.0F, -3.0F, 0.5236F, 0.0F, 0.1309F));
		PartDefinition cube_r7 = head_r1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(26, 48).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, -10.0F, 2.0F, -0.2182F, 0.0F, 0.1309F));
		PartDefinition cube_r8 = head_r1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 47).addBox(0.0F, 2.0F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, -15.0F, -1.0F, 0.0F, 0.0F, -0.1309F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
		this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
	}
}