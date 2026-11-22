package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.MrKeepsEntity;
import net.mcreator.mchol.client.model.ModelMrKeep;

public class MrKeepsRenderer extends MobRenderer<MrKeepsEntity, LivingEntityRenderState, ModelMrKeep> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/mr_keep.png");

	public MrKeepsRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelMrKeep(context.bakeLayer(ModelMrKeep.LAYER_LOCATION)), 0.98f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(MrKeepsEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}