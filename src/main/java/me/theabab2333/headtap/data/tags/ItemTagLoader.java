package me.theabab2333.headtap.data.tags;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumTagsProvider;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import me.theabab2333.headtap.init.item.ModItems;
import net.minecraft.world.item.Item;

public class ItemTagLoader {
    public static void init(RegistrumTagsProvider<Item> provider) {
        provider.addTag(ModItemTags.ANVIL_HAMMER)
            .add(ModItems.AMETHYST_HAMMER.getKey());
    }
}
