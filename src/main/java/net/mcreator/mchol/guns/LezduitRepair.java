package net.mcreator.mchol.guns;

import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.ChatFormatting;

import net.mcreator.mchol.entity.MerkEntity;

import javax.annotation.Nullable;

import java.util.Optional;

/**
 * Like in the game, Lezduit is found broken and Gene is the one who fixes him: Merks killed by a player rarely drop a
 * broken Lezduit (custom data flag), which can't fire until Gene repairs it in his shop.
 */
@EventBusSubscriber
public class LezduitRepair {
	public static final String BROKEN_KEY = "mchol_lezduit_broken";
	/** Chance that a Merk killed by a player drops a broken Lezduit. */
	private static final float MERK_DROP_CHANCE = 0.01f;
	/** Gene's repair price in emeralds (paid together with the broken gun). */
	private static final int REPAIR_EMERALDS = 20;

	@Nullable
	private static Item lezduit() {
		return BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("mchol", "lezduit")).orElse(null);
	}

	private static CustomData brokenData() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean(BROKEN_KEY, true);
		return CustomData.of(tag);
	}

	@Nullable
	public static ItemStack broken() {
		Item item = lezduit();
		if (item == null)
			return null;
		ItemStack stack = new ItemStack(item);
		stack.set(DataComponents.CUSTOM_DATA, brokenData());
		return stack;
	}

	public static boolean isBroken(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		return data != null && data.copyTag().getBooleanOr(BROKEN_KEY, false);
	}

	/** Gene's offer: broken Lezduit + emeralds -> working Lezduit. Empty when the item isn't registered yet. */
	public static Optional<MerchantOffer> repairOffer(int maxUses) {
		Item item = lezduit();
		if (item == null)
			return Optional.empty();
		ItemCost brokenGun = new ItemCost(item.builtInRegistryHolder(), 1, DataComponentExactPredicate.expect(DataComponents.CUSTOM_DATA, brokenData()));
		return Optional.of(new MerchantOffer(brokenGun, Optional.of(new ItemCost(Items.EMERALD, REPAIR_EMERALDS)), new ItemStack(item), maxUses, 10, 0.0f));
	}

	/** A broken Lezduit only crackles; called by GunHandler instead of firing. */
	static void onTriggerBroken(ServerPlayer player) {
		player.sendOverlayMessage(Component.translatableWithFallback("hud.mchol.lezduit_broken", "Lezduit is broken. Gene can repair him.").withStyle(ChatFormatting.RED));
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.PLAYERS, 0.5f, 1.6f);
	}

	@SubscribeEvent
	public static void onDrops(LivingDropsEvent event) {
		LivingEntity entity = event.getEntity();
		if (!(entity instanceof MerkEntity) || !(event.getSource().getEntity() instanceof Player) || entity.getRandom().nextFloat() >= MERK_DROP_CHANCE)
			return;
		ItemStack stack = broken();
		if (stack == null)
			return;
		ItemEntity drop = new ItemEntity(entity.level(), entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
		drop.setDefaultPickUpDelay();
		event.getDrops().add(drop);
	}
}
