package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import me.theabab2333.headtap.init.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Objects;

public abstract class AbstractBaseMachineBlockEntity extends BlockEntity implements IFilterBlockEntity, IItemHandlerHolder, MenuProvider {
    public AbstractBaseMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public abstract int cooldown();
    public abstract int slotCount();
    public abstract int shouldSkipSlot();
    public abstract boolean isEnabled();
    public abstract void setDirection(Direction direction);
    public abstract void setEnabled(boolean enabled);

    public final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(slotCount()) {
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot < shouldSkipSlot() ? super.extractItem(shouldSkipSlot(), amount, simulate) : ItemStack.EMPTY;
        }
    };

    private int cd = cooldown();

    @Override
    public FilteredItemStackHandler getFilteredItemStackHandler() {
        return itemHandler;
    }

    @Override
    public abstract Component getDisplayName();

    @Override
    public abstract AbstractContainerMenu createMenu(int i, Inventory inventory, Player player);

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    public Direction getOutputDirection() {
        return getBlockState().getValue(ModBlockStateProperties.OUTPUT_DIRECTION);
    }

    /**
     * 机器 tick
     */
    public abstract void tick();

    /**
     * 机器自动输出 tick
     */
    public void autoOutput() {
        if (level == null) return;
        if (getBlockState().getValue(ModBlockStateProperties.OUTPUT_ENABLE)) {
            cd--;
            if (cd <= 0) {
                cd = 5;
                if (isEnabled()) {
                    IItemHandler cap = Objects.requireNonNull(getLevel()).getCapability(
                        Capabilities.ItemHandler.BLOCK,
                        getBlockPos().relative(getOutputDirection()),
                        getOutputDirection().getOpposite());
                    if (cap != null) {
                        for (int i = shouldSkipSlot(); i < itemHandler.getSlots(); i++) {
                            ItemHandlerUtil.insertItem(cap, itemHandler.getStackInSlot(i), false);
                        }
                    }
                }
            }
        }
    }
}
