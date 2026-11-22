package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.DouglasEntity;
import net.mcreator.mchol.client.model.ModelDouglas;

public class DouglasRenderer extends MobRenderer<DouglasEntity, LivingEntityRenderState, ModelDouglas> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/douglas.png");

	public DouglasRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelDouglas(context.bakeLayer(ModelDouglas.LAYER_LOCATION)), 0.63f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(DouglasEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}