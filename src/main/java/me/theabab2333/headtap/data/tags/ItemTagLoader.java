package me.theabab2333.headtap.data.tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import me.theabab2333.headtap.init.ModItems;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ItemTagLoader {
    public static void init(@NotNull RegistrateTagsProvider<Item> provider) {
        provider.addTag(ModItemTags.ANVIL_HAMMER)
            .add(ModItems.AMETHYST_HAMMER.getKey());
    }
}
