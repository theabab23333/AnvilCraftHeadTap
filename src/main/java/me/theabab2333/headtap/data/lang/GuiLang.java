package me.theabab2333.headtap.data.lang;


import dev.anvilcraft.lib.v2.registrum.providers.RegistrumLangProvider;

public class GuiLang {
    public static void init(RegistrumLangProvider provider) {
        provider.add("gui.headtap.builder.top", "Builder");
        provider.add("gui.headtap.artificial_high_temperature_device.top", "Artificial High Temperature Device");
        provider.add("screen.headtap.button.output", "Output ItemStacks: %s");
    }
}
