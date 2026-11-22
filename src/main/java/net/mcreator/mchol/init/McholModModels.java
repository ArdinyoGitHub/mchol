/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.resources.Identifier;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;

import net.mcreator.mchol.client.model.*;

@EventBusSubscriber(Dist.CLIENT)
public class McholModModels {
	public static final ModelLayerLocation ZEPHYRA_BLUE_BOAT_LAYER_LOCATION = new ModelLayerLocation(Identifier.parse("mchol:boat/zephyra_blue_boat"), "main");
	public static final ModelLayerLocation ZEPHYRA_BLUE_CHEST_BOAT_LAYER_LOCATION = new ModelLayerLocation(Identifier.parse("mchol:chest_boat/zephyra_blue_chest_boat"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelDouglas.LAYER_LOCATION, ModelDouglas::createBodyLayer);
		event.registerLayerDefinition(ModelMytes.LAYER_LOCATION, ModelMytes::createBodyLayer);
		event.registerLayerDefinition(ModelChairAlien.LAYER_LOCATION, ModelChairAlien::createBodyLayer);
		event.registerLayerDefinition(ModelCreatureMinion.LAYER_LOCATION, ModelCreatureMinion::createBodyLayer);
		event.registerLayerDefinition(ModelCustomModel.LAYER_LOCATION, ModelCustomModel::createBodyLayer);
		event.registerLayerDefinition(ModelMrKeep.LAYER_LOCATION, ModelMrKeep::createBodyLayer);
		event.registerLayerDefinition(Modelmerk.LAYER_LOCATION, Modelmerk::createBodyLayer);
		event.registerLayerDefinition(ModelKrubis.LAYER_LOCATION, ModelKrubis::createBodyLayer);
		event.registerLayerDefinition(ZEPHYRA_BLUE_BOAT_LAYER_LOCATION, BoatModel::createBoatModel);
		event.registerLayerDefinition(ZEPHYRA_BLUE_CHEST_BOAT_LAYER_LOCATION, BoatModel::createChestBoatModel);
	}
}