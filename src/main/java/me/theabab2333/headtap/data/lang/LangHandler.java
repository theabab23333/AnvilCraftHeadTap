package me.theabab2333.headtap.data.lang;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumLangProvider;

public class LangHandler {
    public static void init(RegistrumLangProvider provider) {
        JeiLang.init(provider);
        ItemTooltipLang.init(provider);
        OtherLang.init(provider);
        JadeLang.init(provider);
        GuiLang.init(provider);
    }
}
