package me.theabab2333.headtap.data.tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;

public class TagsHandler {
    public static void initItem(RegistrateTagsProvider<Item> provider) {
        ItemTagLoader.init(provider);
    }
}
