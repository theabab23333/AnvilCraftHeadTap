package me.theabab2333.headtap.api.tooltip;

import com.google.common.collect.Maps;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModItems;
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
        map.put(ModItems.AMETHYST_HAMMER.asItem(), "Fragile crystal, deals more damage.");
        map.put(ModItems.BAMBOO_JAVELIN.asItem(), "The less durability it has, the less damage it deals.");
        map.put(ModItems.GOLEM_CRAFTBOW.asItem(), "Right click to heal nearby Iron Golems with Iron Ingots. \nShift + Right click to summon golems with material blocks.");
        map.put(ModItems.RESIN_FLUID_BUCKET.asItem(), "A cauldron of resin turns into a block when output via a hopper.");

        map.put(ModBlocks.AMETHYST_ANVIL.asItem(), "Fragile but pleasant anvil.");
        //我想之后试试把紫水晶砧落地的声音改成紫水晶的音效
        map.put(ModBlocks.ANVIL_OBSERVER.asItem(), "Emits redstone signal when anvil lands on the block in front of it.");
        map.put(ModBlocks.BUILDER.asItem(), "Put an itemframe on top of this block, and put a multi-block crafting result in the itemframe will assign the recipe. \nPlaces blocks of the multi-block recipe when enough materials and redstone powered.");
        map.put(ModBlocks.DISTRIBUTER.asItem(), "Distribute item inputs into two halves and output items through two faces. \nDisabled when redstone powered.");
        map.put(ModBlocks.ENTITY_EJECTOR.asItem(), "Stores elastic energy when anvil lands on it, and ejects entity on it with that energy when redstone powered.\n\"Significant weightlessness!\"");
        map.put(ModBlocks.PASSIVE_ROYAL_ANVIL.asItem(), "Functions as a Royal Anvil for items in it when hit by anvil.");
        map.put(ModBlocks.PASSIVE_ROYAL_GRINDSTONE.asItem(), "Functions as a Royal Grindstone for items in it when hit by anvil.");
        map.put(ModBlocks.PASSIVE_ROYAL_TABLE.asItem(), "Functions as a Royal Smithing Table for items in it when hit by anvil.");
        map.put(ModBlocks.PRINTER.asItem(), "With books, blessed/cursed gold ingots in this block, copies the enchantment book upon this block when hit by anvil.");
        map.put(ModBlocks.RESIN_EXTRACTOR.asItem(), "Extracts resin from nearby tree when hit by an anvil.\nAutomatically outputs resin fluid to nearby cauldrons.");
        map.put(ModBlocks.STONE_GENERATOR.asItem(), "When there are appropriate blocks and liquids around it, it generates stone when hit by anvil.");
        map.put(ModBlocks.VARIABLE_FLUID_TANK.asItem(), "When there are ore blocks around it, its storage capacity increases.");
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
