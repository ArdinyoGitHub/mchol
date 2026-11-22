package net.mcreator.mchol.jetpack;

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

/** Sent by the client whenever the jetpack state changes, so the server can handle fall damage and effects. */
@EventBusSubscriber
public record JetpackStateMessage(int state) implements CustomPacketPayload {
	public static final int OFF = 0;
	public static final int HOVER = 1;
	public static final int THRUST = 2;

	public static final Type<JetpackStateMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(McholMod.MODID, "jetpack_state"));
	public static final StreamCodec<RegistryFriendlyByteBuf, JetpackStateMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, JetpackStateMessage message) -> buffer.writeVarInt(message.state),
			(RegistryFriendlyByteBuf buffer) -> new JetpackStateMessage(buffer.readVarInt()));

	@Override
	public Type<JetpackStateMessage> type() {
		return TYPE;
	}

	public static void handleData(final JetpackStateMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer player)
					Jetpack.setState(player, message.state);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		McholMod.addNetworkMessage(JetpackStateMessage.TYPE, JetpackStateMessage.STREAM_CODEC, JetpackStateMessage::handleData);
	}
}
