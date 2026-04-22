package me.theabab2333.headtap.data.tags;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagsHandler {
    public static void initItem(RegistrumTagsProvider<Item> provider) {
        ItemTagLoader.init(provider);
    }

    public static void initBlock(RegistrumTagsProvider<Block> provider) {
        BlockTagLoader.init(provider);
    }
}
