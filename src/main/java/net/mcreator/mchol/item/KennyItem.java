package net.mcreator.mchol.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.network.chat.Component;

import net.mcreator.mchol.entity.GlobEntity;

public class KennyItem extends Item {
	public KennyItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
		return ItemUseAnimation.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
		return 72000;
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		ensureInitialNBT(stack);
		return stack;
	}

	private void ensureInitialNBT(ItemStack stack) {
		if (stack.isEmpty()) {
			return;
		}
		CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
			nbt.putDouble("ammo", 10);
			nbt.putDouble("shotsFired", 0);
		});
	}

	private double getAmmo(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("ammo", 10);
	}

	private double getShotsFired(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("shotsFired", 0);
	}

	private void setAmmo(ItemStack stack, double ammo) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble("ammo", ammo));
	}

	private void setShotsFired(ItemStack stack, double shots) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble("shotsFired", shots));
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		InteractionResult ar = InteractionResult.FAIL;
		ItemStack stack = entity.getItemInHand(hand);
		ensureInitialNBT(stack);
		if (entity.getAbilities().instabuild || canShoot(stack)) {
			ar = InteractionResult.CONSUME;
			entity.startUsingItem(hand);
			if (entity instanceof ServerPlayer serverPlayer) {
				serverPlayer.sendSystemMessage(Component.literal("KennyItem.use() called. Shots: " + getShotsFired(stack)));
			}
		}
		return ar;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ensureInitialNBT(itemstack);
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		// Apply cooldown after using
		if (entity instanceof Player player) {
		double shots = getShotsFired(itemstack);
		// Check if reload was triggered (5 shots fired)
		if (shots >= 5) {
			// Apply reload cooldown: 60 ticks (3 seconds)
			player.getCooldowns().addCooldown(itemstack, 60);
			// Reset shots counter for next reload cycle
			setShotsFired(itemstack, 0);
		} else {
			// Normal cooldown (20 ticks = 1 second)
			player.getCooldowns().addCooldown(itemstack, 20);
		}
		}
		return retval;
	}

	@Override
	public boolean releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
		ensureInitialNBT(itemstack);
		if (!world.isClientSide() && entity instanceof ServerPlayer player) {
			if (player.getAbilities().instabuild || canShoot(itemstack)) {
				GlobEntity projectile = GlobEntity.shoot(world, entity, world.getRandom());
				if (player.getAbilities().instabuild) {
					projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
				} else {
					// Decrement ammo and increment shotsFired
					double shots = getShotsFired(itemstack) + 1;
					setShotsFired(itemstack, shots);
					double ammo = getAmmo(itemstack) - 1;
					setAmmo(itemstack, ammo);
					if (shots >= 5) {
						// Reload after 5 shots
						setAmmo(itemstack, 10);
						player.sendSystemMessage(Component.literal("Kenny: Reloaded! Shots reset."));
					}
				}
			}
		}
		return super.releaseUsing(itemstack, world, entity, time);
	}

	private boolean canShoot(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return false;
		}
		// Only check ammo, not shotsFired
		// shotsFired is managed in releaseUsing and finishUsingItem
		double ammo = getAmmo(stack);
		return ammo > 0;
	}
}
