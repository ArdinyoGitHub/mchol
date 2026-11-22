package net.mcreator.mchol.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.SectionPos;

import net.mcreator.mchol.procedures.JetpackOnKeyReleaseddProcedure;
import net.mcreator.mchol.procedures.JetpackOnKeyPressedProcedure;
import net.mcreator.mchol.McholMod;

@EventBusSubscriber
public record JetpackMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<JetpackMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(McholMod.MODID, "key_jetpack"));
	public static final StreamCodec<RegistryFriendlyByteBuf, JetpackMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, JetpackMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new JetpackMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<JetpackMessage> type() {
		return TYPE;
	}

	public static void handleData(final JetpackMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.pressedms);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (type == 0) {

			JetpackOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {

			JetpackOnKeyReleaseddProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		McholMod.addNetworkMessage(JetpackMessage.TYPE, JetpackMessage.STREAM_CODEC, JetpackMessage::handleData);
	}
}