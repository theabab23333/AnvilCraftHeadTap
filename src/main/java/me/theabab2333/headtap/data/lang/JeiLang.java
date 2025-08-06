package me.theabab2333.headtap.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class JeiLang {
    public static void init(RegistrateLangProvider provider) {
        provider.add("jei.headtap.category.stone_generator", "Stone Generator");
        provider.add("jei.headtap.category.ejector", "Eject");
        provider.add("jei.headtap.category.ejector.high", "Required Height: %s");
    }
}
