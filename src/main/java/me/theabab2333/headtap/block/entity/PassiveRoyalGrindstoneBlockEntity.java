package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import dev.dubhe.anvilcraft.init.ModItems;
import me.theabab2333.headtap.api.GetEnchantments;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PassiveRoyalGrindstoneBlockEntity extends BlockEntity implements IItemHandlerHolder, IFilterBlockEntity {

    public static final Item REPAIR_MATERIAL = Items.GOLD_INGOT;
    public static final Item RESULT_MATERIAL = ModItems.CURSED_GOLD_INGOT.get();
    public static final int GOLD_PER_CURSE = 16;

    public int totalCurseCount = 0;

    public final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(4) {

        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == 0) {
                return super.insertItem(0, stack, simulate);
            } else if (slot == 1) {
                return super.insertItem(1, stack, simulate);
            } else {
                return stack;
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 2) {
                return super.extractItem(2, amount, simulate);
            } else if (slot == 3) {
                return super.extractItem(3, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return stack.is(REPAIR_MATERIAL);
            } else if (slot == 2) {
                return stack.is(RESULT_MATERIAL);
            } else {
                return true;
            }
        }
    };

    public void createResult() {
        if (level == null) return;
        int hasGold = itemHandler.getStackInSlot(0).getCount();
        ItemStack repairItem = itemHandler.getStackInSlot(1);
        if (hasGold == 0 || repairItem.isEmpty()) return;
        ItemStack result = repairItem.copy();
        int repairCost = repairItem.getOrDefault(DataComponents.REPAIR_COST, 0);
        int goldUsed = 0;
        goldUsed += repairCost;
        DataComponentType<ItemEnchantments> enchantmentComponent = GetEnchantments.getEnchantmentComponent(result);
        ItemEnchantments enchantments = GetEnchantments.getItemEnchantments(result);
        this.totalCurseCount = 0;
        if (enchantments != null) {
            this.totalCurseCount = GetEnchantments.getCurseCount(result);
            ItemEnchantments.Mutable mutEnch = GetEnchantments.getMutableEnchantments(enchantments);
            Iterator<Holder<Enchantment>> iterator = GetEnchantments.getIterator(mutEnch);
            while (iterator.hasNext() && hasGold >= GOLD_PER_CURSE) {
                Holder<Enchantment> curseEnchantment = iterator.next();
                if (!curseEnchantment.is(EnchantmentTags.CURSE)) continue;
                iterator.remove();
                goldUsed += GOLD_PER_CURSE;
            }
            result.set(enchantmentComponent, mutEnch.toImmutable());
        }
        if (result.is(Items.ENCHANTED_BOOK) && !EnchantmentHelper.hasAnyEnchantments(result)) {
            result = result.transmuteCopy(Items.BOOK);
        }
        if (hasGold >= goldUsed) {
            result.set(DataComponents.REPAIR_COST, 0);
            itemHandler.getStackInSlot(0).setCount(hasGold - goldUsed);
            itemHandler.getStackInSlot(1).setCount(0);
            int count = itemHandler.getStackInSlot(2).getCount() + goldUsed;
            itemHandler.setStackInSlot(2, new ItemStack(ModItems.CURSED_GOLD_INGOT.get(), count));
            itemHandler.setStackInSlot(3, result);
        }
    }

    public PassiveRoyalGrindstoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public FilteredItemStackHandler getFilteredItemDepository() {
        return itemHandler;
    }

    public FilteredItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.PASSIVE_ROYAL_GRINDSTONE.get(),
            (be, context) -> be.itemHandler
        );
    }
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inv", itemHandler.serializeNBT(provider));
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inv"));
    }
}
