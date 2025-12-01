package me.theabab2333.headtap.network;

import lombok.Getter;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.client.gui.screen.IOutputScreen;
import me.theabab2333.headtap.inventory.IOutputMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.jetbrains.annotations.NotNull;

@Getter
public class MachineEnableOutputPacket implements CustomPacketPayload {
    public static final Type<MachineEnableOutputPacket> TYPE = new Type<>(HeadTap.of("machine_output"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MachineEnableOutputPacket> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.BOOL,
            MachineEnableOutputPacket::isOutputEnabled,
            MachineEnableOutputPacket::new
        );
    public static final IPayloadHandler<MachineEnableOutputPacket> HANDLER = new DirectionalPayloadHandler<>(
        MachineEnableOutputPacket::clientHandler,
        MachineEnableOutputPacket::serverHandler
    );

    private final boolean outputEnabled;

    public MachineEnableOutputPacket(boolean isEnabled) {
        this.outputEnabled = isEnabled;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void serverHandler(MachineEnableOutputPacket data, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        context.enqueueWork(() -> {
            if (!player.hasContainerOpen()) return;
            if (!(player.containerMenu instanceof IOutputMenu menu)) return;
            menu.setOutputEnable(data.isOutputEnabled());
            menu.flush();
            PacketDistributor.sendToPlayer(player, data);
        });
    }

    public static void clientHandler(MachineEnableOutputPacket data, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        context.enqueueWork(() -> {
            if (client.screen instanceof IOutputScreen<?> screen) {
                screen.setOutputEnable(data.isOutputEnabled());
                screen.flush();
            }
        });
    }
}
