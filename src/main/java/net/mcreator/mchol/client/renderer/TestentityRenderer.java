package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.TestentityEntity;
import net.mcreator.mchol.client.model.ModelCustomModel;

public class TestentityRenderer extends MobRenderer<TestentityEntity, LivingEntityRenderState, ModelCustomModel> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/merkxture.png");

	public TestentityRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCustomModel(context.bakeLayer(ModelCustomModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(TestentityEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}