package me.theabab2333.headtap.api.tooltips;

import com.google.common.collect.Maps;
import me.theabab2333.headtap.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemTooltipManager {
    private static final Map<Item, String> map = Maps.newHashMap();
    public static final Map<Item, String> NEED_TOOLTIP_ITEM = Collections.unmodifiableMap(map);


    static {
        map.put(ModBlocks.ENTITY_EJECTOR.asItem(), "significant weightlessness");
    }

    public static void addTooltip(ItemStack stack, List<Component> tooltip) {
        Item item = stack.getItem();
        if (NEED_TOOLTIP_ITEM.containsKey(item)) {
            tooltip.add(1, getItemTooltip(item));
        }
    }

    private static Component getItemTooltip(Item item) {
        return Component.translatable(getTranslationKey(item)).withStyle(ChatFormatting.GRAY);
    }

    public static String getTranslationKey(Item item) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        return "tooltip.%s.item.%s".formatted(key.getNamespace(), key.getPath());
    }
}
