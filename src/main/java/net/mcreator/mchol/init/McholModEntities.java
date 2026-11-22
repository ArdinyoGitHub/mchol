/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mchol.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.mchol.entity.*;
import net.mcreator.mchol.McholMod;

@EventBusSubscriber
public class McholModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, McholMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<GlobEntity>> GLOB = register("glob",
			EntityType.Builder.<GlobEntity>of(GlobEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<CrystalEntity>> CRYSTAL = register("crystal",
			EntityType.Builder.<CrystalEntity>of(CrystalEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<GusGlobEntity>> GUS_GLOB = register("gus_glob",
			EntityType.Builder.<GusGlobEntity>of(GusGlobEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<CreatureChildrenEntity>> CREATURE_CHILDREN = register("creature_children",
			EntityType.Builder.<CreatureChildrenEntity>of(CreatureChildrenEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<MerkEntity>> MERK = register("merk",
			EntityType.Builder.<MerkEntity>of(MerkEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<Glob2Entity>> GLOB_2 = register("glob_2",
			EntityType.Builder.<Glob2Entity>of(Glob2Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<Glob3Entity>> GLOB_3 = register("glob_3",
			EntityType.Builder.<Glob3Entity>of(Glob3Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<BlueMyteEntity>> BLUE_MYTE = register("blue_myte",
			EntityType.Builder.<BlueMyteEntity>of(BlueMyteEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<TestentityEntity>> TESTENTITY = register("testentity",
			EntityType.Builder.<TestentityEntity>of(TestentityEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<ZephyraBlueBoatEntity>> ZEPHYRA_BLUE_BOAT = register("zephyra_blue_boat",
			EntityType.Builder.<ZephyraBlueBoatEntity>of(ZephyraBlueBoatEntity::new, MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<ZephyraBlueChestBoatEntity>> ZEPHYRA_BLUE_CHEST_BOAT = register("zephyra_blue_chest_boat",
			EntityType.Builder.<ZephyraBlueChestBoatEntity>of(ZephyraBlueChestBoatEntity::new, MobCategory.MISC).noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<KrubisEntity>> KRUBIS = register("krubis",
			EntityType.Builder.<KrubisEntity>of(KrubisEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(96).setUpdateInterval(3)

					.notInPeaceful().sized(1f, 3f));
	public static final DeferredHolder<EntityType<?>, EntityType<DouglasEntity>> DOUGLAS = register("douglas",
			EntityType.Builder.<DouglasEntity>of(DouglasEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(96).setUpdateInterval(3)

					.notInPeaceful().sized(0.9f, 2.7f));
	public static final DeferredHolder<EntityType<?>, EntityType<GeneEntity>> GENE = register("gene",
			EntityType.Builder.<GeneEntity>of(GeneEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(1.1f, 2.9f));
	public static final DeferredHolder<EntityType<?>, EntityType<MrKeepsEntity>> MR_KEEPS = register("mr_keeps",
			EntityType.Builder.<MrKeepsEntity>of(MrKeepsEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(1.4f, 2.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<CreatureMinionEntity>> CREATURE_MINION = register("creature_minion",
			EntityType.Builder.<CreatureMinionEntity>of(CreatureMinionEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.45f, 0.75f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(McholMod.MODID, registryname))));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		MerkEntity.init(event);
		BlueMyteEntity.init(event);
		TestentityEntity.init(event);
		KrubisEntity.init(event);
		DouglasEntity.init(event);
		GeneEntity.init(event);
		MrKeepsEntity.init(event);
		CreatureMinionEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(MERK.get(), MerkEntity.createAttributes().build());
		event.put(BLUE_MYTE.get(), BlueMyteEntity.createAttributes().build());
		event.put(TESTENTITY.get(), TestentityEntity.createAttributes().build());
		event.put(KRUBIS.get(), KrubisEntity.createAttributes().build());
		event.put(DOUGLAS.get(), DouglasEntity.createAttributes().build());
		event.put(GENE.get(), GeneEntity.createAttributes().build());
		event.put(MR_KEEPS.get(), MrKeepsEntity.createAttributes().build());
		event.put(CREATURE_MINION.get(), CreatureMinionEntity.createAttributes().build());
	}
}