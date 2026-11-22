package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.KrubisEntity;
import net.mcreator.mchol.client.model.ModelKrubis;

public class KrubisRenderer extends MobRenderer<KrubisEntity, LivingEntityRenderState, ModelKrubis> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/krubis.png");

	public KrubisRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelKrubis(context.bakeLayer(ModelKrubis.LAYER_LOCATION)), 0.7f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(KrubisEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}