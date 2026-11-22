package net.mcreator.mchol.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.EntityType;

import net.mcreator.mchol.init.McholModItems;

public class ZephyraBlueChestBoatEntity extends ChestBoat {
	public ZephyraBlueChestBoatEntity(EntityType<ZephyraBlueChestBoatEntity> type, Level world) {
		super(type, world, McholModItems.ZEPHYRA_BLUE_CHEST_BOAT);
	}
}