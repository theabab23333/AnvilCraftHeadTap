package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import dev.dubhe.anvilcraft.init.ModItems;
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
public class AutoRoyalGrindstoneEntity extends BlockEntity implements IItemHandlerHolder, IFilterBlockEntity {

    public static final int GOLD_PER_CURSE = 16;
    public static final Item REPAIR_MATERIAL = Items.GOLD_INGOT;
    public static final Item RESULT_MATERIAL = ModItems.CURSED_GOLD_INGOT.get();

    public int usedGold = 0;
    public int totalRepairCost = 0;
    public int totalCurseCount = 0;
    public int removedRepairCost = 0;
    public int removedCurseCount = 0;

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

    public ItemStack createResult() {
        assert level != null;
        ItemStack repairItem = itemHandler.getStackInSlot(1);
        ItemStack repairMaterials = itemHandler.getStackInSlot(0);
        if (repairItem.isEmpty() || repairMaterials.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = repairItem.copy();
        int repairCost = repairItem.getOrDefault(DataComponents.REPAIR_COST, 0);
        this.totalRepairCost = repairCost;
        int goldUsed = 0;
        int goldUsable = Math.min(repairMaterials.getCount(),
            RESULT_MATERIAL.getDefaultMaxStackSize() - itemHandler.getStackInSlot(2).getCount());
        int removedRepairCost = Math.min(repairCost, goldUsable);
        goldUsed += removedRepairCost;
        goldUsable -= removedRepairCost;
        int remainRepairCost = repairCost - removedRepairCost;
        result.set(DataComponents.REPAIR_COST, remainRepairCost);
        int removedCurseCount = 0;
        DataComponentType<ItemEnchantments> enchantmentComponent =
            result.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
        ItemEnchantments enchantments = result.get(enchantmentComponent);
        this.totalCurseCount = 0;
        if (enchantments != null) {
            this.totalCurseCount = (int) enchantments.keySet()
                .stream()
                .filter(it -> it.is(EnchantmentTags.CURSE))
                .count();
            ItemEnchantments.Mutable mutEnch = new ItemEnchantments.Mutable(enchantments);
            Iterator<Holder<Enchantment>> iterator = mutEnch.keySet().iterator();
            while (iterator.hasNext() && goldUsable >= GOLD_PER_CURSE) {
                Holder<Enchantment> curseEnchantment = iterator.next();
                if (!curseEnchantment.is(EnchantmentTags.CURSE)) continue;
                iterator.remove();
                goldUsed += GOLD_PER_CURSE;
                goldUsable -= GOLD_PER_CURSE;
                removedCurseCount += 1;
            }
            result.set(enchantmentComponent, mutEnch.toImmutable());
        }
        if (result.is(Items.ENCHANTED_BOOK) && !EnchantmentHelper.hasAnyEnchantments(result)) {
            result = result.transmuteCopy(Items.BOOK);
        }
        this.usedGold = goldUsed;
        this.removedCurseCount = removedCurseCount;
        this.removedRepairCost = removedRepairCost;
        return result;
    }

    public AutoRoyalGrindstoneEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
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
            ModBlockEntities.AUTO_ROYAL_GRINDSTONE.get(),
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
