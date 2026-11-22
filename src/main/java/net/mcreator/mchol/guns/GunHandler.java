package net.mcreator.mchol.guns;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.ChatFormatting;

import java.util.UUID;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

/** Server side gun logic: magazines, fire rate, reloading and blocking vanilla bow/melee behaviour. */
@EventBusSubscriber
public class GunHandler {
	public static final String PROJECTILE_TAG = "mchol_gun_projectile";
	private static final String LIFETIME_KEY = "mchol_gun_lifetime";

	private static final Map<UUID, Map<Item, GunState>> STATES = new HashMap<>();

	private static class GunState {
		int ammo;
		long reloadEnd = -1;
		long nextShot;
		long nextAlt;

		GunState(int ammo) {
			this.ammo = ammo;
		}

		boolean reloading() {
			return reloadEnd >= 0;
		}
	}

	private static GunState state(Player player, ItemStack stack, GunSpec spec) {
		return STATES.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).computeIfAbsent(stack.getItem(), k -> new GunState(spec.magazine()));
	}

	public static void tryFire(ServerPlayer player) {
		ItemStack stack = player.getMainHandItem();
		GunSpec spec = GunSpec.get(stack);
		if (spec == null || player.isSpectator())
			return;
		if (LezduitRepair.isBroken(stack)) {
			LezduitRepair.onTriggerBroken(player);
			return;
		}
		Level level = player.level();
		long now = level.getGameTime();
		GunState state = state(player, stack, spec);
		finishReload(player, spec, state, now);
		if (state.reloading() || now < state.nextShot)
			return;
		if (state.ammo <= 0) {
			startReload(player, stack, spec, state, now);
			return;
		}
		state.ammo--;
		state.nextShot = now + spec.fireInterval();
		Vec3 look = player.getLookAngle();
		if (spec.projectile() == null) {
			LezduitBeam.fire(player, spec, look, now);
		} else {
			for (int i = 0; i < spec.pellets(); i++)
				spawnProjectile(player, spec, look, spec.speed(), spec.inaccuracy(), spec.damage(), spec.lifetime(), spec.gravity());
			SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(spec.sound()));
			if (sound != null)
				level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 1, 0.9f + level.getRandom().nextFloat() * 0.2f);
		}
		spawnMuzzleFlash(player, spec, look);
		PacketDistributor.sendToPlayer(player, new GunMessage(GunMessage.SHOT));
		if (state.ammo <= 0)
			startReload(player, stack, spec, state, now);
		else
			showAmmo(player, stack, spec, state);
	}

	/** Also used by Merks (see merk/MerkGuns), so the shooter can be any living entity. */
	public static AbstractArrow spawnProjectile(LivingEntity shooter, GunSpec spec, Vec3 direction, float speed, float inaccuracy, double damage, int lifetime, boolean gravity) {
		AbstractArrow projectile = spec.projectile().create(shooter.level(), shooter);
		projectile.setNoGravity(!gravity);
		projectile.shoot(direction.x, direction.y, direction.z, speed, inaccuracy);
		// arrow damage is multiplied by its speed on impact
		projectile.setBaseDamage(damage / speed);
		projectile.setCritArrow(false);
		projectile.setSilent(true);
		projectile.pickup = AbstractArrow.Pickup.DISALLOWED;
		projectile.addTag(PROJECTILE_TAG);
		projectile.getPersistentData().putInt(LIFETIME_KEY, lifetime);
		shooter.level().addFreshEntity(projectile);
		return projectile;
	}

	public static void tryAltFire(ServerPlayer player) {
		ItemStack stack = player.getMainHandItem();
		GunSpec spec = GunSpec.get(stack);
		AltFire alt = AltFire.get(stack);
		if (spec == null || alt == null || player.isSpectator())
			return;
		long now = player.level().getGameTime();
		GunState state = state(player, stack, spec);
		if (now < state.nextAlt)
			return;
		state.nextAlt = now + alt.cooldown;
		alt.fire(player, spec);
		spawnMuzzleFlash(player, spec, player.getLookAngle());
		PacketDistributor.sendToPlayer(player, new GunMessage(GunMessage.SHOT));
		showAmmo(player, stack, spec, state);
	}

	private static void spawnMuzzleFlash(ServerPlayer player, GunSpec spec, Vec3 look) {
		if (!(player.level() instanceof ServerLevel level))
			return;
		// roughly where the barrel is: in front of the eyes, a bit to the side of the main hand and down
		Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize().scale(player.getMainArm() == HumanoidArm.RIGHT ? 0.35 : -0.35);
		Vec3 muzzle = player.getEyePosition().add(look.scale(1.0)).add(right).add(0, -0.25, 0);
		level.sendParticles(new DustParticleOptions(spec.muzzleColor(), 0.8f), muzzle.x, muzzle.y, muzzle.z, 4, 0.05, 0.05, 0.05, 0);
	}

	public static void tryReload(ServerPlayer player) {
		ItemStack stack = player.getMainHandItem();
		GunSpec spec = GunSpec.get(stack);
		if (spec == null)
			return;
		long now = player.level().getGameTime();
		GunState state = state(player, stack, spec);
		finishReload(player, spec, state, now);
		startReload(player, stack, spec, state, now);
	}

	private static void startReload(ServerPlayer player, ItemStack stack, GunSpec spec, GunState state, long now) {
		if (state.reloading() || state.ammo >= spec.magazine())
			return;
		state.reloadEnd = now + spec.reloadTicks();
		// shows the white cooldown overlay on the hotbar slot
		player.getCooldowns().addCooldown(stack, spec.reloadTicks());
		if (spec.projectile() != null) // Lezduit's storm already thunders
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 0.8f, 1.2f);
		showAmmo(player, stack, spec, state);
	}

	private static boolean finishReload(ServerPlayer player, GunSpec spec, GunState state, long now) {
		if (!state.reloading() || now < state.reloadEnd)
			return false;
		state.ammo = spec.magazine();
		state.reloadEnd = -1;
		return true;
	}

	private static void showAmmo(ServerPlayer player, ItemStack stack, GunSpec spec, GunState state) {
		Component status;
		if (LezduitRepair.isBroken(stack))
			status = Component.translatableWithFallback("hud.mchol.broken", "Broken").withStyle(ChatFormatting.RED);
		else if (spec.projectile() == null) // Lezduit: the magazine is a 5 s charge, the reload is the recharge
			status = state.reloading()
					? Component.translatableWithFallback("hud.mchol.recharging", "Recharging").withStyle(ChatFormatting.GRAY)
							.append(Component.literal(String.format(Locale.ROOT, " %.1fs", Math.max(0, state.reloadEnd - player.level().getGameTime()) / 20f)))
					: Component.translatableWithFallback("hud.mchol.charge", "Charge").withStyle(ChatFormatting.LIGHT_PURPLE)
							.append(Component.literal(String.format(Locale.ROOT, " %d%%", Math.round(100f * state.ammo / spec.magazine()))));
		else
			status = state.reloading()
				? Component.translatableWithFallback("hud.mchol.reloading", "Reloading...").withStyle(ChatFormatting.YELLOW)
				: Component.literal(state.ammo + " / " + spec.magazine()).withStyle(state.ammo <= spec.magazine() / 4 ? ChatFormatting.RED : ChatFormatting.WHITE);
		MutableComponent message = stack.getHoverName().copy().withStyle(ChatFormatting.GOLD).append(Component.literal("   ")).append(status);
		AltFire alt = AltFire.get(stack);
		if (alt != null) {
			long left = state.nextAlt - player.level().getGameTime();
			Component altName = Component.translatableWithFallback("hud.mchol.alt." + alt.name().toLowerCase(Locale.ROOT), alt.displayName);
			message.append(Component.literal("   |   ").withStyle(ChatFormatting.DARK_GRAY));
			if (left > 0)
				message.append(altName.copy().withStyle(ChatFormatting.GRAY)).append(Component.literal(String.format(Locale.ROOT, " %.1fs", left / 20f)).withStyle(ChatFormatting.GRAY));
			else
				message.append(altName.copy().withStyle(ChatFormatting.GREEN)).append(Component.literal(" ✔").withStyle(ChatFormatting.GREEN));
		}
		player.sendOverlayMessage(message);
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;
		ItemStack stack = player.getMainHandItem();
		GunSpec spec = GunSpec.get(stack);
		if (spec == null)
			return;
		GunState state = state(player, stack, spec);
		if (finishReload(player, spec, state, player.level().getGameTime())) {
			if (spec.projectile() == null)
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.8f, 1.6f);
			else
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 0.8f, 1.2f);
			showAmmo(player, stack, spec, state);
		} else if (player.tickCount % 10 == 0) {
			// keep the action bar visible while the gun is held
			showAmmo(player, stack, spec, state);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		STATES.remove(event.getEntity().getUUID());
	}

	/** Guns are fired with left click, so right click must not start the old bow-like use. */
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (GunSpec.get(event.getItemStack()) != null) {
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.FAIL);
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (GunSpec.get(event.getEntity().getMainHandItem()) != null)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onAttackEntity(AttackEntityEvent event) {
		if (GunSpec.get(event.getEntity().getMainHandItem()) != null)
			event.setCanceled(true);
	}

	/** Without this, only the first shotgun pellet would deal damage because of the hurt cooldown. */
	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		Entity direct = event.getSource().getDirectEntity();
		if (LezduitBeam.HURTING || direct != null && direct.entityTags().contains(PROJECTILE_TAG))
			event.getContainer().setPostAttackInvulnerabilityTicks(1); // must be > 0; only more than 10 ticks blocks further hits
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		if (entity.level().isClientSide() || !entity.entityTags().contains(PROJECTILE_TAG))
			return;
		AltFire.trail(entity);
		if (entity.tickCount > entity.getPersistentData().getIntOr(LIFETIME_KEY, 100)) {
			AltFire.onExpired(entity);
			CreatureMinions.onProjectileExpired(entity);
			entity.discard();
		}
	}
}
