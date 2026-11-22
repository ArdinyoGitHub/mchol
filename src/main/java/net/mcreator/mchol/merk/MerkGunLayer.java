package net.mcreator.mchol.merk;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.Minecraft;

import net.mcreator.mchol.init.McholModEntities;
import net.mcreator.mchol.entity.MerkEntity;
import net.mcreator.mchol.client.renderer.MerkRenderer;
import net.mcreator.mchol.client.model.Modelmerk;
import net.mcreator.mchol.McholMod;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Draws the Merk's gun in its right hand. MerkRenderer is MCreator generated (no item layer, plain LivingEntityRenderState),
 * so the held item is attached to the render state and the layer is added from here.
 */
public class MerkGunLayer extends RenderLayer<LivingEntityRenderState, Modelmerk> {
	private static final ContextKey<ItemStackRenderState> HELD_ITEM = new ContextKey<>(Identifier.fromNamespaceAndPath(McholMod.MODID, "merk_held_item"));
	/** Distance from the shoulder pivot to the hand, in model pixels (the Merk arm is 21 px long). */
	private static final float HAND_OFFSET = 19.0F;

	public MerkGunLayer(RenderLayerParent<LivingEntityRenderState, Modelmerk> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LivingEntityRenderState state, float yRot, float xRot) {
		ItemStackRenderState item = state.getRenderData(HELD_ITEM);
		if (item == null || item.isEmpty())
			return;
		poseStack.pushPose();
		Modelmerk model = this.getParentModel();
		model.root().translateAndRotate(poseStack);
		model.rightarm.translateAndRotate(poseStack);
		// same hand transform as vanilla ItemInHandLayer, with the grip moved to the end of the longer arm
		poseStack.rotate(Axis.XP.rotationDegrees(-90.0F));
		poseStack.rotate(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(0.0F, 2.0F / 16.0F, -HAND_OFFSET / 16.0F);
		item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
	}

	@EventBusSubscriber(value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
			event.<MerkEntity, LivingEntityRenderState>registerEntityModifier(MerkRenderer.class, (entity, state) -> {
				if (entity.getMainHandItem().isEmpty()) {
					state.setRenderData(HELD_ITEM, null);
					return;
				}
				ItemStackRenderState item = new ItemStackRenderState();
				Minecraft.getInstance().getItemModelResolver().updateForLiving(item, entity.getMainHandItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
				state.setRenderData(HELD_ITEM, item);
			});
		}

		@SubscribeEvent
		public static void addLayers(EntityRenderersEvent.AddLayers event) {
			if (event.getRenderer(McholModEntities.MERK.get()) instanceof MerkRenderer renderer)
				renderer.addLayer(new MerkGunLayer(renderer));
		}
	}
}
