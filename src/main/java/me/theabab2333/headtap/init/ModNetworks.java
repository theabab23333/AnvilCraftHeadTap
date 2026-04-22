package me.theabab2333.headtap.init;

import me.theabab2333.headtap.network.MachineEnableOutputPacket;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworks {
    public static void init(PayloadRegistrar registrar) {
        registrar.playBidirectional(
            MachineEnableOutputPacket.TYPE,
            MachineEnableOutputPacket.STREAM_CODEC,
            MachineEnableOutputPacket.HANDLER
        );
    }
}
