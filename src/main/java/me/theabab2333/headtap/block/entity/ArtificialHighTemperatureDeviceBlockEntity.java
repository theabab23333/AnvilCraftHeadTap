package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import me.theabab2333.headtap.init.ModMenuTypes;
import me.theabab2333.headtap.init.block.ModBlocks;
import me.theabab2333.headtap.inventory.MachineOutputMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ArtificialHighTemperatureDeviceBlockEntity extends AbstractBaseMachineBlockEntity {
    public ArtificialHighTemperatureDeviceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public int cooldown() {
        return 5;
    }

    @Override
    public int slotCount() {
        return 18;
    }

    @Override
    public int shouldSkipSlot() {
        return 9;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.headtap.artificial_high_temperature_device.top");
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (player.isSpectator()) return null;
        return new MachineOutputMenu(ModMenuTypes.MACHINE_OUTPUT.get(), i, inventory, this);
    }

    @Override
    public void tick() {
        itemHandler.setStackInSlot(9, ModBlocks.BUILDER.asStack());
        autoOutput();
    }

    @Override
    public FilteredItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
