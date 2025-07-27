package me.theabab2333.headtap.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import me.theabab2333.headtap.api.tooltips.ItemTooltipManager;

public class ItemTooltipLang {
    public static void init(RegistrateLangProvider provider) {
        ItemTooltipManager.NEED_TOOLTIP_ITEM.forEach(
            ((item, s) -> provider.add(ItemTooltipManager.getTranslationKey(item), s))
        );
        provider.add("item.headtap.amethyst_hammer.tooltip", "Fragile crystal, deals more damage.");
        provider.add("item.headtap.golem_craftbow.tooltip", "Right click to heal nearby Iron Golems with Iron Ingots. \n Shift + Right click to summon golems with material blocks.");
    }
}
