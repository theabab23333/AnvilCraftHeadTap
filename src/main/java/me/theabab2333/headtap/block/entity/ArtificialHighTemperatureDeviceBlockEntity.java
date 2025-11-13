package me.theabab2333.headtap.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class ArtificialHighTemperatureDeviceBlockEntity extends AbstractBaseMachineBlockEntity {
    public ArtificialHighTemperatureDeviceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected int cooldown() {
        return 5;
    }

    @Override
    protected int slotCount() {
        return 18;
    }

    @Override
    protected Direction getOutputDirection() {
        return Direction.UP;
    }

    @Override
    protected int shouldSkipSlot() {
        return 9;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public void tick() {
        autoOutput();
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}
