package net.mcreator.mchol.guns;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.mchol.McholMod;

/**
 * Client to server: FIRE while the trigger is held, RELOAD when the reload key is pressed, ALT_FIRE on right click.
 * Server to client: SHOT after a shot was actually fired, to play the recoil animation.
 */
@EventBusSubscriber
public record GunMessage(int action) implements CustomPacketPayload {
	public static final int FIRE = 0;
	public static final int RELOAD = 1;
	public static final int SHOT = 2;
	public static final int ALT_FIRE = 3;

	public static final Type<GunMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(McholMod.MODID, "gun_action"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GunMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, GunMessage message) -> buffer.writeVarInt(message.action),
			(RegistryFriendlyByteBuf buffer) -> new GunMessage(buffer.readVarInt()));

	@Override
	public Type<GunMessage> type() {
		return TYPE;
	}

	public static void handleData(final GunMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (!(context.player() instanceof ServerPlayer player))
					return;
				if (message.action == FIRE)
					GunHandler.tryFire(player);
				else if (message.action == RELOAD)
					GunHandler.tryReload(player);
				else if (message.action == ALT_FIRE)
					GunHandler.tryAltFire(player);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		} else if (message.action == SHOT) {
			context.enqueueWork(() -> GunClientEvents.onShot());
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		McholMod.addNetworkMessage(GunMessage.TYPE, GunMessage.STREAM_CODEC, GunMessage::handleData);
	}
}
