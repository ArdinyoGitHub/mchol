/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.BoatRenderer;

import net.mcreator.mchol.client.renderer.*;

@EventBusSubscriber(Dist.CLIENT)
public class McholModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(McholModEntities.GLOB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(McholModEntities.CRYSTAL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(McholModEntities.GUS_GLOB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(McholModEntities.CREATURE_CHILDREN.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(McholModEntities.MERK.get(), MerkRenderer::new);
		event.registerEntityRenderer(McholModEntities.GLOB_2.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(McholModEntities.GLOB_3.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(McholModEntities.BLUE_MYTE.get(), BlueMyteRenderer::new);
		event.registerEntityRenderer(McholModEntities.TESTENTITY.get(), TestentityRenderer::new);
		event.registerEntityRenderer(McholModEntities.ZEPHYRA_BLUE_BOAT.get(), context -> new BoatRenderer(context, McholModModels.ZEPHYRA_BLUE_BOAT_LAYER_LOCATION));
		event.registerEntityRenderer(McholModEntities.ZEPHYRA_BLUE_CHEST_BOAT.get(), context -> new BoatRenderer(context, McholModModels.ZEPHYRA_BLUE_CHEST_BOAT_LAYER_LOCATION));
		event.registerEntityRenderer(McholModEntities.KRUBIS.get(), KrubisRenderer::new);
		event.registerEntityRenderer(McholModEntities.DOUGLAS.get(), DouglasRenderer::new);
		event.registerEntityRenderer(McholModEntities.GENE.get(), GeneRenderer::new);
		event.registerEntityRenderer(McholModEntities.MR_KEEPS.get(), MrKeepsRenderer::new);
		event.registerEntityRenderer(McholModEntities.CREATURE_MINION.get(), CreatureMinionRenderer::new);
	}
}