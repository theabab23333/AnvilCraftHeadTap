package me.theabab2333.headtap.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class GuiLang {
    public static void init(RegistrateLangProvider provider) {
        provider.add("gui.headtap.builder.top", "Builder");
        provider.add("gui.headtap.artificial_high_temperature_device.top", "Artificial High Temperature Device");
        provider.add("screen.headtap.button.output", "Output ItemStacks: %s");
    }
}
