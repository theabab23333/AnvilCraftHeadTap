package me.theabab2333.headtap.api;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Iterator;

public class GetEnchantments {
    public static DataComponentType<ItemEnchantments> getEnchantmentComponent(ItemStack itemStack) {
        return itemStack.is(Items.ENCHANTED_BOOK) ?
            DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
    }

    public static ItemEnchantments getItemEnchantments(ItemStack itemStack) {
        DataComponentType<ItemEnchantments> enchantmentComponent = getEnchantmentComponent(itemStack);
        return getItemEnchantments(itemStack, enchantmentComponent);
    }

    private static ItemEnchantments getItemEnchantments(ItemStack itemStack, DataComponentType<ItemEnchantments> enchantmentComponent) {
        ItemEnchantments enchantments = itemStack.get(enchantmentComponent);
        if (enchantments == null) return ItemEnchantments.EMPTY;
        return enchantments;
    }

    public static ItemEnchantments.Mutable getMutableEnchantments(ItemEnchantments enchantments) {
        return new ItemEnchantments.Mutable(enchantments);
    }

    public static ItemEnchantments.Mutable getMutableEnchantments(ItemStack itemStack) {
        return new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack));
    }

    public static Iterator<Holder<Enchantment>> getIterator(ItemEnchantments.Mutable mutable) {
        return mutable.keySet().iterator();
    }

    public static int calculateTheSumOfAllEnchantments(ItemStack itemStack) {
        int level = 0;
        ItemEnchantments enchantments = getItemEnchantments(itemStack);
        ItemEnchantments.Mutable inputEnchantments = getMutableEnchantments(itemStack);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            level += inputEnchantments.getLevel(holder);
        }
        return level;
    }

    public static int calculateTheSumOfAllCruse(ItemStack itemStack) {
        int level = 0;
        ItemEnchantments enchantments = getItemEnchantments(itemStack);
        ItemEnchantments.Mutable inputEnchantments = getMutableEnchantments(itemStack);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(EnchantmentTags.CURSE))
                level += inputEnchantments.getLevel(holder);
        }
        return level;
    }

    public static int calculateTheSumOfOther(ItemStack itemStack) {
        int level = 0;
        ItemEnchantments enchantments = getItemEnchantments(itemStack);
        ItemEnchantments.Mutable inputEnchantments = getMutableEnchantments(itemStack);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (!holder.is(EnchantmentTags.CURSE))
                level += inputEnchantments.getLevel(holder);
        }
        return level;
    }

    @SuppressWarnings("deprecation")
    public static Object2IntMap.Entry<Holder<Enchantment>> getEntry(ItemEnchantments enchantments) {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            return entry;
        } return null;
    }

    public static int getAllCount(ItemStack itemStack) {
        int count;
        ItemEnchantments enchantments = getItemEnchantments(itemStack);
        count = enchantments.keySet()
            .size();
        return count;
    }

    public static int getCurseCount(ItemStack itemStack) {
        int count;
        ItemEnchantments enchantments = getItemEnchantments(itemStack);
        count = (int) enchantments.keySet()
            .stream()
            .filter(it -> it.is(EnchantmentTags.CURSE))
            .count();
        return count;
    }

    public static int getOtherCount(ItemStack itemStack) {
        int count;
        ItemEnchantments enchantments = getItemEnchantments(itemStack);
        count = (int) enchantments.keySet()
            .stream()
            .filter(it -> !it.is(EnchantmentTags.CURSE))
            .count();
        return count;
    }

    public static String getString(Holder<Enchantment> holder) {
        return holder.value().toString();
    }
}
