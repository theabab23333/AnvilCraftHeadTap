package me.theabab2333.head_tap.init;

import me.theabab2333.head_tap.Head_tap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.theabab2333.head_tap.Head_tap.REGISTRATE;

@SuppressWarnings("unused")
public class ModItemGroups {
    private static final DeferredRegister<CreativeModeTab> ET =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Head_tap.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ANVILCRAFT_HEAD_TAP =
            ET.register("block", () -> CreativeModeTab.builder()
                    .icon(ModBlocks.AMETHYST_ANVIL::asStack)
                    .title(REGISTRATE.addLang("itemGroup", Head_tap.of("block"), "ANVILCRAFT: HEAD TAP"))
                    .build());
    public static void register(IEventBus modEventBus) {
        ET.register(modEventBus);
    }
}
