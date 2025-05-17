package me.theabab2333.head_tap.event;


import me.theabab2333.head_tap.Head_tap;
import me.theabab2333.head_tap.init.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;

@EventBusSubscriber(modid = Head_tap.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilitiesEventListener {
    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
        List.of(
            ModBlockEntities.STONE_GENERATOR.get()
        ).forEach(type -> event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            type,
            (be, side) -> be.getItemHandler())
        );
    }
}
