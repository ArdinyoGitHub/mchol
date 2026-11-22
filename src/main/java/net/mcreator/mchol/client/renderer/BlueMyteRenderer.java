package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.BlueMyteEntity;
import net.mcreator.mchol.client.model.ModelMytes;

public class BlueMyteRenderer extends MobRenderer<BlueMyteEntity, LivingEntityRenderState, ModelMytes> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/myte1.png");

	public BlueMyteRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelMytes(context.bakeLayer(ModelMytes.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(BlueMyteEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}