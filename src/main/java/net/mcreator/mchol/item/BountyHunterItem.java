package net.mcreator.mchol.item;

import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.mchol.procedures.ChestplateProcedure;
import net.mcreator.mchol.procedures.BountyHunterLeggingsTickEventProcedure;
import net.mcreator.mchol.procedures.BountyHunterHelmetTickEventProcedure;
import net.mcreator.mchol.procedures.BountyHunterBootsTickEventProcedure;

import javax.annotation.Nullable;

import java.util.Map;

public abstract class BountyHunterItem extends Item {
	public static ArmorMaterial ARMOR_MATERIAL = new ArmorMaterial(30, Map.of(ArmorType.BOOTS, 4, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 7, ArmorType.HELMET, 5, ArmorType.BODY, 7), 9,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY), 1f, 0.2f, TagKey.create(Registries.ITEM, Identifier.parse("mchol:bounty_hunter_repair_items")),
			ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("mchol:bounty_hunter")));

	private BountyHunterItem(Item.Properties properties) {
		super(properties);
	}

	public static class Helmet extends BountyHunterItem {
		public Helmet(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.HELMET).fireResistant());
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				BountyHunterHelmetTickEventProcedure.execute(entity);
			}
		}
	}

	public static class Chestplate extends BountyHunterItem {
		public Chestplate(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.CHESTPLATE).fireResistant());
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				ChestplateProcedure.execute(world, entity);
			}
		}
	}

	public static class Leggings extends BountyHunterItem {
		public Leggings(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.LEGGINGS).fireResistant());
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				BountyHunterLeggingsTickEventProcedure.execute(world, entity);
			}
		}
	}

	public static class Boots extends BountyHunterItem {
		public Boots(Item.Properties properties) {
			super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.BOOTS).fireResistant());
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				BountyHunterBootsTickEventProcedure.execute(world, itemstack);
			}
		}
	}
}