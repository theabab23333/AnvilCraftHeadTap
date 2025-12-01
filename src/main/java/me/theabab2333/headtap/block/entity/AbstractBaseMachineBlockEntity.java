package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import dev.dubhe.anvilcraft.block.entity.BaseMachineBlockEntity;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Objects;

@Getter
public abstract class AbstractBaseMachineBlockEntity extends BaseMachineBlockEntity implements IFilterBlockEntity, IOutputEntity {
    public AbstractBaseMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public abstract int cooldown();
    public abstract int slotCount();
    public abstract int shouldSkipSlot();
    public abstract void setDirection(Direction direction);

    @Setter
    private boolean outputEnabled = false;
    private int cd = cooldown();

    public final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(slotCount()) {
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot < shouldSkipSlot() ? super.extractItem(shouldSkipSlot(), amount, simulate) : ItemStack.EMPTY;
        }
    };

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
        tag.putBoolean("OutputEnabled", outputEnabled);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.outputEnabled = tag.getBoolean("OutputEnabled");
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
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
        if (this.isOutputEnabled()) {
            cd--;
            if (cd <= 0) {
                cd = 5;
                if (this.isOutputEnabled()) {
                    IItemHandler cap = Objects.requireNonNull(getLevel()).getCapability(
                        Capabilities.ItemHandler.BLOCK,
                        getBlockPos().relative(getDirection()),
                        getDirection().getOpposite());
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
