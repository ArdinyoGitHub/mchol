// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelCustomModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "custommodel"), "main");
	private final ModelPart sagkol;
	private final ModelPart solkol;
	private final ModelPart sagbacak;
	private final ModelPart solbacak;
	private final ModelPart kafa;
	private final ModelPart vucut;

	public ModelCustomModel(ModelPart root) {
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

		PartDefinition sagkol = partdefinition.addOrReplaceChild("sagkol", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, 12.0F, 0.0F));

		PartDefinition solkol = partdefinition.addOrReplaceChild("solkol", CubeListBuilder.create().texOffs(0, 0)
				.addBox(7.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, 12.0F, 0.0F));

		PartDefinition sagbacak = partdefinition.addOrReplaceChild("sagbacak", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 24.0F, 0.0F));

		PartDefinition solbacak = partdefinition.addOrReplaceChild("solbacak", CubeListBuilder.create().texOffs(0, 0)
				.addBox(3.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 24.0F, 0.0F));

		PartDefinition kafa = partdefinition.addOrReplaceChild("kafa", CubeListBuilder.create().texOffs(-5, -3).addBox(
				-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, 0.0F));

		PartDefinition vucut = partdefinition.addOrReplaceChild("vucut", CubeListBuilder.create().texOffs(-6, 0)
				.addBox(1.0F, -11.0F, -1.0F, 6.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		sagkol.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		solkol.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sagbacak.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		solbacak.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		kafa.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		vucut.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.kafa.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.kafa.xRot = headPitch / (180F / (float) Math.PI);
		this.solkol.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.solbacak.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.sagkol.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.sagbacak.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}