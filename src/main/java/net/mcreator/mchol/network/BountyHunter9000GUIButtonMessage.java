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

import net.mcreator.mchol.procedures.*;
import net.mcreator.mchol.McholMod;

@EventBusSubscriber
public record BountyHunter9000GUIButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<BountyHunter9000GUIButtonMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(McholMod.MODID, "bounty_hunter_9000_gui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, BountyHunter9000GUIButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, BountyHunter9000GUIButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new BountyHunter9000GUIButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<BountyHunter9000GUIButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final BountyHunter9000GUIButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleButtonAction(context.player(), message.buttonID, message.x, message.y, message.z)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (buttonID == 0) {

			ZepyhrtpProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {

			GatlustpProcedure.execute(entity);
		}
		if (buttonID == 2) {

			NethertpProcedure.execute(entity);
		}
		if (buttonID == 3) {

			EndtpProcedure.execute(entity);
		}
		if (buttonID == 4) {

			OverworldtpProcedure.execute(entity);
		}
		if (buttonID == 5) {

			PortpProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		McholMod.addNetworkMessage(BountyHunter9000GUIButtonMessage.TYPE, BountyHunter9000GUIButtonMessage.STREAM_CODEC, BountyHunter9000GUIButtonMessage::handleData);
	}
}