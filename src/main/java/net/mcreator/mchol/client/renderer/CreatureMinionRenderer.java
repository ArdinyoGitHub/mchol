package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.CreatureMinionEntity;
import net.mcreator.mchol.client.model.ModelCreatureMinion;

import com.mojang.blaze3d.vertex.PoseStack;

public class CreatureMinionRenderer extends MobRenderer<CreatureMinionEntity, LivingEntityRenderState, ModelCreatureMinion> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/creature_minion.png");

	public CreatureMinionRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCreatureMinion(context.bakeLayer(ModelCreatureMinion.LAYER_LOCATION)), 0.3f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(CreatureMinionEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.42f, 0.42f, 0.42f);
	}
}