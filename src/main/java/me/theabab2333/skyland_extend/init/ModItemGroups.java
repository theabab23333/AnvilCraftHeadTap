package me.theabab2333.skyland_extend.init;

import dev.anvilcraft.skyland.Skyland;
import me.theabab2333.skyland_extend.Skyland_extend;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.theabab2333.skyland_extend.Skyland_extend.REGISTRATE;

@SuppressWarnings("unused")
public class ModItemGroups {
    private static final DeferredRegister<CreativeModeTab> ET =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Skyland_extend.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SKYLAND_EXTEND_BLOCK =
            ET.register("block", () -> CreativeModeTab.builder()
                    .icon(ModBlocks.AMETHYST_ANVIL::asStack)
                    .title(REGISTRATE.addLang("itemGroup", Skyland_extend.of("block"), "Skyland-Extend: Block"))
                    .withTabsBefore(
                            Skyland.of("anvilcraft_skyland")
                    )
                    .build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SKYLAND_EXTEND_ITEM =
            ET.register("item", () -> CreativeModeTab.builder()
                    .icon(ModItems.AMETHYST_HAMMER::asStack)
                    .title(REGISTRATE.addLang("itemGroup", Skyland_extend.of("item"), "Skyland Extend: Item"))
                    .withTabsBefore(
                            Skyland_extend.of("block")
                    )
                    .build());

    public static void register(IEventBus modEventBus) {
        ET.register(modEventBus);
    }
}
