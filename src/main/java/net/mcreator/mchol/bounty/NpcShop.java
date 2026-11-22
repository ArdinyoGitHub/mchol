package net.mcreator.mchol.bounty;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.mchol.guns.LezduitRepair;
import net.mcreator.mchol.McholMod;

import javax.annotation.Nullable;

import java.util.Optional;

/**
 * Server side shop of Gene (Bounty Hunter armor, Knifey, Lezduit repair) and Mr. Keeps (gun upgrade units). A new one is made every time a player
 * opens the shop, so stock never runs out and nothing has to be saved on the entity.
 */
public class NpcShop implements Merchant {
	private static final int MAX_USES = 999;
	/** Players further away than this (from the shopkeeper's feet) lose the trade screen. */
	private static final double MAX_DISTANCE_SQR = 10 * 10;

	private final Mob keeper;
	private final Player player;
	private MerchantOffers offers;

	public NpcShop(Mob keeper, Player player, BountyEntities kind) {
		this.keeper = keeper;
		this.player = player;
		this.offers = offersFor(kind);
	}

	public static MerchantOffers offersFor(BountyEntities kind) {
		MerchantOffers offers = new MerchantOffers();
		if (kind == BountyEntities.GENE) {
			// half the prices of the old level 5 Zephyr villager trades
			add(offers, "bounty_hunter_helmet", 12, Items.DIAMOND, 2);
			add(offers, "bounty_hunter_chestplate", 24, Items.FIREWORK_ROCKET, 8);
			add(offers, "bounty_hunter_leggings", 16, Items.DIAMOND, 2);
			add(offers, "bounty_hunter_boots", 10, Items.DIAMOND, 1);
			// Knifey, same price as at the Zephyr villager
			add(offers, "knifey", 12, Items.IRON_SWORD, 1);
			// in the game Gene is the one who repaired Lezduit: he doesn't sell him, he fixes the broken one Merks drop
			LezduitRepair.repairOffer(MAX_USES).ifPresent(offers::add);
		} else if (kind == BountyEntities.MR_KEEPS) {
			add(offers, "multi_glob_unit", 20, Items.GLOWSTONE_DUST, 8);
			add(offers, "goop_suck_unit", 28, Items.SLIME_BALL, 8);
			add(offers, "portal_unit", 36, Items.ENDER_PEARL, 4);
		}
		return offers;
	}

	private static void add(MerchantOffers offers, String result, int emeralds, Item extra, int extraCount) {
		Optional<Item> item = BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath(McholMod.MODID, result));
		if (item.isEmpty())
			return;
		offers.add(new MerchantOffer(new ItemCost(Items.EMERALD, emeralds), Optional.of(new ItemCost(extra, extraCount)), new ItemStack(item.get()), MAX_USES, 10, 0.0f));
	}

	@Override
	public void setTradingPlayer(@Nullable Player player) {
	}

	@Override
	public @Nullable Player getTradingPlayer() {
		return player;
	}

	@Override
	public MerchantOffers getOffers() {
		return offers;
	}

	@Override
	public void overrideOffers(MerchantOffers offers) {
		this.offers = offers;
	}

	@Override
	public void notifyTrade(MerchantOffer offer) {
		offer.increaseUses();
		keeper.playSound(SoundEvents.VILLAGER_YES, 1.0f, keeper.getVoicePitch());
		Level level = keeper.level();
		if (offer.shouldRewardExp() && level instanceof ServerLevel serverLevel)
			ExperienceOrb.award(serverLevel, keeper.position().add(0, 1.5, 0), offer.getXp());
	}

	@Override
	public void notifyTradeUpdated(ItemStack itemStack) {
		keeper.playSound(itemStack.isEmpty() ? SoundEvents.VILLAGER_NO : SoundEvents.VILLAGER_YES, 0.6f, keeper.getVoicePitch());
	}

	@Override
	public int getVillagerXp() {
		return 0;
	}

	@Override
	public void overrideXp(int xp) {
	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@Override
	public SoundEvent getNotifyTradeSound() {
		return SoundEvents.VILLAGER_YES;
	}

	@Override
	public boolean isClientSide() {
		return keeper.level().isClientSide();
	}

	@Override
	public boolean stillValid(Player player) {
		return player == this.player && keeper.isAlive() && player.distanceToSqr(keeper) < MAX_DISTANCE_SQR;
	}
}
