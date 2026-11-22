package net.mcreator.mchol.village;

import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.tags.StructureTags;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.mchol.bounty.NpcShops;
import net.mcreator.mchol.McholMod;

import java.util.Map;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

/**
 * Puts one Zephyr villager (sells the BH9K) in every newly generated overworld village, Gene and Mr. Keeps in every
 * newly generated Zephyr village (mchol:zephyr_village),
 * and loads the trade list from a built-in data pack because MCreator rewrites its own (empty) trade tag on every build.
 */
@EventBusSubscriber
public class ZephyrVillagers {
	public static final ResourceKey<VillagerProfession> PROFESSION = ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(McholMod.MODID, "zephyrvillager"));
	/** Ticks to wait after the village chunk loaded, so the village's own villagers and the terrain around it are in place. */
	private static final int SPAWN_DELAY = 40;
	/** Pending spawns whose chunk stays unloaded this long are dropped. */
	private static final int GIVE_UP_TICKS = 20 * 60;

	/** The village structure in the Zephyr dimension; Gene and Mr. Keeps live only there. */
	public static final ResourceKey<Structure> ZEPHYR_VILLAGE = ResourceKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(McholMod.MODID, "zephyr_village"));

	private record PendingSpawn(ResourceKey<Level> dimension, BlockPos center, long createdAt, boolean zephyrVillage) {
	}

	// chunk events can arrive from worker threads
	private static final Queue<PendingSpawn> PENDING = new ConcurrentLinkedQueue<>();

	@SubscribeEvent
	public static void addPackFinders(AddPackFindersEvent event) {
		event.addPackFinders(Identifier.fromNamespaceAndPath(McholMod.MODID, "packs/zephyr_trades"), PackType.SERVER_DATA, Component.literal("Zephyr villager trades"), PackSource.BUILT_IN, true, Pack.Position.TOP);
	}

	@EventBusSubscriber
	public static class GameEvents {
		@SubscribeEvent
		public static void onChunkLoad(ChunkEvent.Load event) {
			if (!event.isNewChunk() || !(event.getLevel() instanceof ServerLevel level) || !(event.getChunk() instanceof LevelChunk chunk))
				return;
			boolean overworld = level.dimension() == Level.OVERWORLD;
			var structures = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
			// a structure start is stored only in the chunk the village starts in, so every village is seen exactly once
			for (Map.Entry<Structure, StructureStart> entry : chunk.getAllStarts().entrySet()) {
				StructureStart start = entry.getValue();
				if (!start.isValid() || start.getPieces().isEmpty())
					continue;
				var holder = structures.wrapAsHolder(entry.getKey());
				BlockPos center = start.getPieces().getFirst().getBoundingBox().getCenter();
				if (holder.is(ZEPHYR_VILLAGE))
					PENDING.add(new PendingSpawn(level.dimension(), center, level.getGameTime(), true));
				else if (overworld && holder.is(StructureTags.VILLAGE))
					PENDING.add(new PendingSpawn(level.dimension(), center, level.getGameTime(), false));
			}
		}

		@SubscribeEvent
		public static void onServerTick(ServerTickEvent.Post event) {
			MinecraftServer server = event.getServer();
			for (Iterator<PendingSpawn> it = PENDING.iterator(); it.hasNext();) {
				PendingSpawn pending = it.next();
				ServerLevel level = server.getLevel(pending.dimension());
				if (level == null) {
					it.remove();
					continue;
				}
				long age = level.getGameTime() - pending.createdAt();
				if (age < SPAWN_DELAY)
					continue;
				if (level.isLoaded(pending.center())) {
					if (pending.zephyrVillage())
						NpcShops.spawnInVillage(level, pending.center());
					else
						spawnZephyrVillager(level, pending.center());
					it.remove();
				} else if (age > GIVE_UP_TICKS) {
					it.remove();
				}
			}
		}
	}

	private static void spawnZephyrVillager(ServerLevel level, BlockPos center) {
		Villager villager = EntityTypes.VILLAGER.create(level, EntitySpawnReason.STRUCTURE);
		if (villager == null)
			return;
		BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, center);
		villager.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.getRandom().nextFloat() * 360, 0);
		villager.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
		villager.setVillagerData(villager.getVillagerData().withProfession(level.registryAccess(), PROFESSION));
		// without any experience a villager that has no job site block nearby loses its profession
		villager.setVillagerXp(1);
		level.addFreshEntity(villager);
	}
}
