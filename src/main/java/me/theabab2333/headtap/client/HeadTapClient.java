package me.theabab2333.headtap.client;

import me.theabab2333.headtap.client.init.ModModelLayers;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModFluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@Mod(value = HeadTap.MOD_ID, dist = Dist.CLIENT)
public class HeadTapClient {

    public HeadTapClient(IEventBus modBus) {
        modBus.addListener(HeadTapClient::registerClientExtensions);
        modBus.addListener(ModModelLayers::register);
    }
    public static void registerClientExtensions(RegisterClientExtensionsEvent e) {
        ModFluids.onRegisterFluidType(e);
    }
}
