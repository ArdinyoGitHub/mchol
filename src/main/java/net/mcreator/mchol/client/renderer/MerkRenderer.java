package net.mcreator.mchol.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.mchol.entity.MerkEntity;
import net.mcreator.mchol.client.model.Modelmerk;

import com.mojang.blaze3d.vertex.PoseStack;

public class MerkRenderer extends MobRenderer<MerkEntity, LivingEntityRenderState, Modelmerk> {
	private final Identifier entityTexture = Identifier.parse("mchol:textures/entities/merkxture.png");

	public MerkRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelmerk(context.bakeLayer(Modelmerk.LAYER_LOCATION)), 0.8f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(MerkEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(1.1f, 1.1f, 1.1f);
	}
}