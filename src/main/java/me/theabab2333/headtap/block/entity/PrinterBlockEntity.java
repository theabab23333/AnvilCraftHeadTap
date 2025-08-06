package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.IHasDisplayItem;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import dev.dubhe.anvilcraft.network.UpdateDisplayItemPacket;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.EnchantmentTags;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.atomic.AtomicInteger;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrinterBlockEntity extends BlockEntity implements IHasDisplayItem, IFilterBlockEntity {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private ItemStack displayItemStack = null;
    private final int id;

    private int needCurseGold = 0;
    private int needBlessedGold = 0;
    private final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(4) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot != 3) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return stack;
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == 3 ? super.extractItem(3, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public void onContentsChanged(int slot) {
            if (level != null && !level.isClientSide) {
                PacketDistributor.sendToAllPlayers(new UpdateDisplayItemPacket(displayItemStack, getPos()));
            }
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return stack.is(ModItems.BLESSED_GOLD_INGOT);
            } else if (slot == 1) {
                return stack.is(dev.dubhe.anvilcraft.init.ModItems.CURSED_GOLD_INGOT);
            } else if (slot == 2) {
                return stack.is(Items.BOOK);
            } else return true;
        }
    };

    public void toPrint() {
        if (level == null) return;
        if (displayItemStack == null) return;

        int inv0 = itemHandler.getStackInSlot(0).getCount();
        int inv1 = itemHandler.getStackInSlot(1).getCount();
        int inv2 = itemHandler.getStackInSlot(2).getCount();
        int total = needBlessedGold + needCurseGold;
        if (inv0 >= needBlessedGold && inv1 >= needCurseGold && inv2 >= 1 && itemHandler.getStackInSlot(3).isEmpty() && total > 0 && !displayItemStack.isEmpty()) {
            itemHandler.getStackInSlot(0).shrink(needBlessedGold);
            itemHandler.getStackInSlot(1).shrink(needCurseGold);
            itemHandler.getStackInSlot(2).shrink(1);
            itemHandler.setStackInSlot(3, displayItemStack);
        }
    }

    public void getBook(ItemStack itemStack) {
        displayItemStack = itemStack;
        getEnchantments(itemStack);
    }

    public void getEnchantments(ItemStack itemStack) {
        DataComponentType<ItemEnchantments> enchantmentComponent = DataComponents.STORED_ENCHANTMENTS;
        ItemEnchantments enchantments = itemStack.get(enchantmentComponent);
        int curseLevel = 0;
        int otherLevel = 0;
        ItemEnchantments.Mutable inputEnchantments =
            new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack));
        if (enchantments != null) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                int i = inputEnchantments.getLevel(holder);
                if (holder.is(EnchantmentTags.CURSE)) {
                    curseLevel += i;
                } else if (!holder.is(EnchantmentTags.CURSE)) {
                    otherLevel += i;
                }
            }
        }

        if (level != null && !level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new UpdateDisplayItemPacket(displayItemStack, getPos()));
        }
        needCurseGold = curseLevel * 4;
        needBlessedGold = otherLevel * 4;
    }

    public PrinterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        id = COUNTER.incrementAndGet();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.PRINTER.get(),
            (be, context) -> be.itemHandler
        );
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("inv", itemHandler.serializeNBT(provider));
        if (displayItemStack != null) tag.put("books", displayItemStack.saveOptional(provider));
        tag.putInt("needB", needBlessedGold);
        tag.putInt("needC", needCurseGold);
        super.saveAdditional(tag, provider);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        itemHandler.deserializeNBT(provider, tag.getCompound("inv"));
        displayItemStack = ItemStack.parseOptional(provider, tag.getCompound("books"));
        needBlessedGold = tag.getInt("needB");
        needCurseGold = tag.getInt("needC");
        super.loadAdditional(tag, provider);
    }

    @Override
    public FilteredItemStackHandler getFilteredItemDepository() {
        return itemHandler;
    }

    public int getId() {
        return this.id;
    }

    public int getNeedCurseGold() {
        return this.needCurseGold;
    }

    public int getNeedBlessedGold() {
        return this.needBlessedGold;
    }

    public BlockPos getPos() {
        return this.getBlockPos();
    }

    public ItemStack getDisplayItemStack() {
        return this.displayItemStack;
    }

    @Override
    public void updateDisplayItem(ItemStack stack) {
        this.displayItemStack = stack;
    }
}
