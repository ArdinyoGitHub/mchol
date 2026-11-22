package net.mcreator.mchol.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.EntityType;

import net.mcreator.mchol.init.McholModItems;

public class ZephyraBlueBoatEntity extends Boat {
	public ZephyraBlueBoatEntity(EntityType<ZephyraBlueBoatEntity> type, Level world) {
		super(type, world, McholModItems.ZEPHYRA_BLUE_BOAT);
	}
}