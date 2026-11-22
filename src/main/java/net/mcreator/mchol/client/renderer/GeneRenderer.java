package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.GeneEntity;
import net.mcreator.mchol.client.model.ModelChairAlien;

public class GeneRenderer extends MobRenderer<GeneEntity, LivingEntityRenderState, ModelChairAlien> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/chair_alien.png");

	public GeneRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelChairAlien(context.bakeLayer(ModelChairAlien.LAYER_LOCATION)), 0.77f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(GeneEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}