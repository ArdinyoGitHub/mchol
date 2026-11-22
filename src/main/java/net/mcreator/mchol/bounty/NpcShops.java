package net.mcreator.mchol.bounty;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

/**
 * Gene and Mr. Keeps: right click opens their shop. They can't be hurt (only /kill removes them) and one of each
 * is placed in every newly generated Zephyr village (mchol:zephyr_village, see ZephyrVillagers), not in overworld villages.
 */
@EventBusSubscriber
public class NpcShops {
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide() && event.getEntity() instanceof Mob mob && BountyEntities.of(mob) instanceof BountyEntities kind && kind.isShopkeeper()) {
			mob.setPermanentlyInvulnerable(true);
			mob.setPersistenceRequired();
		}
	}

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteract event) {
		if (!(event.getTarget() instanceof Mob keeper) || !(BountyEntities.of(keeper) instanceof BountyEntities kind) || !kind.isShopkeeper())
			return;
		Player player = event.getEntity();
		// sneaking keeps vanilla behaviour (name tags, leads, ...)
		if (player.isSecondaryUseActive())
			return;
		event.setCanceled(true);
		event.setCancellationResult(InteractionResult.SUCCESS);
		if (event.getHand() != InteractionHand.MAIN_HAND || player.level().isClientSide())
			return;
		NpcShop shop = new NpcShop(keeper, player, kind);
		if (shop.getOffers().isEmpty())
			return;
		keeper.getLookControl().setLookAt(player);
		shop.openTradingScreen(player, keeper.getDisplayName(), 0);
	}

	/** Called by ZephyrVillagers when a new Zephyr village has generated. */
	public static void spawnInVillage(ServerLevel level, BlockPos center) {
		spawn(level, center.offset(4, 0, 1), BountyEntities.GENE);
		spawn(level, center.offset(-4, 0, -1), BountyEntities.MR_KEEPS);
	}

	private static void spawn(ServerLevel level, BlockPos near, BountyEntities kind) {
		EntityType<?> type = kind.type();
		if (type == null)
			return;
		Entity entity = type.create(level, EntitySpawnReason.STRUCTURE);
		if (entity == null)
			return;
		BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, near);
		entity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.getRandom().nextFloat() * 360, 0);
		if (entity instanceof Mob mob)
			mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
		level.addFreshEntity(entity);
	}
}
