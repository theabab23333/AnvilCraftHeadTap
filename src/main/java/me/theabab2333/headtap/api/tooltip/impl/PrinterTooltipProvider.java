package me.theabab2333.headtap.api.tooltip.impl;

import dev.dubhe.anvilcraft.api.tooltip.providers.ITooltipProvider;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.theabab2333.headtap.block.entity.PrinterBlockEntity;
import me.theabab2333.headtap.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class PrinterTooltipProvider extends ITooltipProvider.BlockEntityTooltipProvider{

    @Override
    public boolean accepts(BlockEntity value) {
        return value instanceof PrinterBlockEntity;
    }

    @Override
    public List<Component> tooltip(BlockEntity value) {
        if (!(value instanceof PrinterBlockEntity printer)) return null;
        List<Component> lines = new ArrayList<>();
        ItemStack itemStack = printer.getDisplayItemStack();
        // make idea angry
        if (itemStack == null) {
            lines.add(Component.translatable("tooltip.headtap.printer.null").withStyle(ChatFormatting.DARK_RED));
            return lines;
        }
        lines.add(Component.translatable("tooltip.headtap.printer.book").withStyle(ChatFormatting.YELLOW));
        DataComponentType<ItemEnchantments> enchantmentComponent = DataComponents.STORED_ENCHANTMENTS;
        ItemEnchantments enchantments = itemStack.get(enchantmentComponent);
        if (enchantments != null) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                int level = entry.getIntValue();
                if (holder.is(EnchantmentTags.CURSE)) {
                    lines.add(Component.translatable(holder.value().toString()).append(String.valueOf(level)).withStyle(ChatFormatting.RED));
                } else lines.add(Component.translatable(holder.value().toString()).append(String.valueOf(level)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }

        int needBCount = printer.getNeedBlessedGold();
        int needCCount = printer.getNeedCurseGold();
        lines.add(Component.translatable("tooltip.headtap.printer.need").withStyle(ChatFormatting.BLUE));
        if (needBCount > 0) lines.add(Component.translatable(ModItems.BLESSED_GOLD_INGOT.asItem().getDescriptionId()).append(String.valueOf(needBCount)).withStyle(ChatFormatting.AQUA));
        if (needCCount > 0) lines.add(Component.translatable(dev.dubhe.anvilcraft.init.ModItems.CURSED_GOLD_INGOT.asItem().getDescriptionId()).append(String.valueOf(needCCount)).withStyle(ChatFormatting.DARK_AQUA));
        lines.add(Component.translatable(Items.BOOK.getDescriptionId()).withStyle(ChatFormatting.GOLD));
        return lines;
    }

    @Override
    public int priority() {
        return 0;
    }
}
