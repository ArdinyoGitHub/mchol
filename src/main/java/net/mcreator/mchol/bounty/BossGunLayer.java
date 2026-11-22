package net.mcreator.mchol.bounty;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import net.mcreator.mchol.McholMod;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.function.BiConsumer;

/**
 * Draws the boss's gun in its right hand. The renderers and models are MCreator generated from the imported Blockbench models,
 * so this layer only relies on the "arm_right" part name, which all bounty models share.
 */
public class BossGunLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
	private static final ContextKey<ItemStackRenderState> HELD_ITEM = new ContextKey<>(Identifier.fromNamespaceAndPath(McholMod.MODID, "boss_held_item"));
	private static final String ARM = "arm_right";

	/** Distance from the shoulder pivot to the palm, in model pixels. */
	private final float handOffset;
	private ModelPart arm;

	public BossGunLayer(RenderLayerParent<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer, float handOffset) {
		super(renderer);
		this.handOffset = handOffset;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LivingEntityRenderState state, float yRot, float xRot) {
		ItemStackRenderState item = state.getRenderData(HELD_ITEM);
		if (item == null || item.isEmpty())
			return;
		EntityModel<LivingEntityRenderState> model = this.getParentModel();
		if (arm == null) {
			if (!model.root().hasChild(ARM))
				return;
			arm = model.root().getChild(ARM);
		}
		poseStack.pushPose();
		model.root().translateAndRotate(poseStack);
		arm.translateAndRotate(poseStack);
		// same hand transform as vanilla ItemInHandLayer, with the grip moved to the end of the arm
		poseStack.rotate(Axis.XP.rotationDegrees(-90.0F));
		poseStack.rotate(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(0.0F, 2.0F / 16.0F, -handOffset / 16.0F);
		item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
	}

	@EventBusSubscriber(value = Dist.CLIENT)
	public static class ClientModEvents {
		@SuppressWarnings({"unchecked", "rawtypes"})
		@SubscribeEvent
		public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
			// runs for every living renderer; only bosses get the held item attached
			BiConsumer<LivingEntity, LivingEntityRenderState> modifier = (entity, state) -> {
				BountyEntities which = BountyEntities.of(entity);
				if (which == null || !which.isBoss())
					return;
				if (entity.getMainHandItem().isEmpty()) {
					state.setRenderData(HELD_ITEM, null);
					return;
				}
				ItemStackRenderState item = new ItemStackRenderState();
				Minecraft.getInstance().getItemModelResolver().updateForLiving(item, entity.getMainHandItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
				state.setRenderData(HELD_ITEM, item);
			};
			event.registerEntityModifier((Class) LivingEntityRenderer.class, modifier);
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		@SubscribeEvent
		public static void addLayers(EntityRenderersEvent.AddLayers event) {
			add(event, BountyEntities.KRUBIS, 12.5F);
			add(event, BountyEntities.DOUGLAS, 11.0F);
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		private static void add(EntityRenderersEvent.AddLayers event, BountyEntities boss, float handOffset) {
			EntityType type = boss.type();
			if (type == null)
				return;
			EntityRenderer renderer = event.getRenderer(type);
			if (renderer instanceof LivingEntityRenderer living)
				living.addLayer(new BossGunLayer(living, handOffset));
		}
	}
}
