package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
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
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AutoAnvilBlockEntity extends BlockEntity implements IItemHandlerHolder, IFilterBlockEntity {

    public static final Item BOOK = Items.ENCHANTED_BOOK;

    public final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(3) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
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
            return slot == 2 ? super.extractItem(2, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return stack.is(BOOK);
            } else if (slot == 1) {
                return stack.is(BOOK);
            } else {
                return true;
            }
        }
    };

    public void createResult() {
        if (level == null) return;
        ItemStack input = itemHandler.getStackInSlot(1);
        ItemStack book = itemHandler.getStackInSlot(0);
        if (!input.isEmpty() && !book.isEmpty()) {
            ItemStack result = input.copy();
            DataComponentType<ItemEnchantments> enchantmentComponent = result.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
            ItemEnchantments enchantments = book.get(enchantmentComponent);
            ItemEnchantments.Mutable inputEnchantments =
                new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(input));
            if (enchantments != null) {
                for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                    Holder<Enchantment> holder = entry.getKey();
                    int i = inputEnchantments.getLevel(holder);
                    int j = entry.getIntValue();
                    j = i == j ? j + 1 : Math.max(j, i);
                    Enchantment enchantment = holder.value();

                    if (!AnvilCraft.config.royalAnvilBeyondMaxLevel && j > enchantment.getMaxLevel()) {
                        j = enchantment.getMaxLevel();
                    }
                    inputEnchantments.set(holder, j);
                }
            }
            EnchantmentHelper.setEnchantments(result, inputEnchantments.toImmutable());
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            itemHandler.setStackInSlot(1, ItemStack.EMPTY);
            itemHandler.setStackInSlot(2, result);
        }
    }

    public AutoAnvilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public FilteredItemStackHandler getFilteredItemDepository() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.PASSIVE_ROYAL_ANVIL.get(),
            (be, context) -> be.itemHandler
        );
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inv", itemHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inv"));
    }
}
