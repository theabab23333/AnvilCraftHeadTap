package me.theabab2333.headtap.api.tooltip.impl;

import dev.dubhe.anvilcraft.api.tooltip.providers.ITooltipProvider;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.theabab2333.headtap.block.entity.PrinterBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
        ItemStack itemStack = printer.displayItemStack;
        if (itemStack == null) {
            lines.add(Component.translatable("tooltip.headtap.printer.null").withStyle(ChatFormatting.DARK_RED));
        } else {
            lines.add(Component.translatable("tooltip.headtap.printer.book").withStyle(ChatFormatting.YELLOW));

            DataComponentType<ItemEnchantments> enchantmentComponent = DataComponents.STORED_ENCHANTMENTS;
            ItemEnchantments enchantments = itemStack.get(enchantmentComponent);
            ItemEnchantments.Mutable inputEnchantments =
                new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack));
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                int i = inputEnchantments.getLevel(holder);
                lines.add(Component.translatable(holder.getRegisteredName(), i));
            }



            lines.add(Component.translatable("tooltip.headtap.printer.need").withStyle(ChatFormatting.BLUE));
        }
        return lines;
    }

    @Override
    public int priority() {
        return 0;
    }
}
