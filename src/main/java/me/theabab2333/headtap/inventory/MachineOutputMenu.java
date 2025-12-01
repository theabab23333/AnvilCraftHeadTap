package me.theabab2333.headtap.inventory;

import dev.dubhe.anvilcraft.inventory.BaseMachineMenu;
import lombok.Getter;
import me.theabab2333.headtap.block.entity.AbstractBaseMachineBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class MachineOutputMenu extends BaseMachineMenu implements IOutputMenu {
    private final AbstractBaseMachineBlockEntity blockEntity;
    private final Level level;

    public MachineOutputMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, @NotNull BlockEntity blockEntity) {
        super(menuType, containerId, blockEntity);
        this.blockEntity = (AbstractBaseMachineBlockEntity) blockEntity;
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    public MachineOutputMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
        this(menuType, containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public void setDirection(Direction direction) {
        if (blockEntity instanceof AbstractBaseMachineBlockEntity entity){
            entity.setDirection(direction);
        }
    }
}
