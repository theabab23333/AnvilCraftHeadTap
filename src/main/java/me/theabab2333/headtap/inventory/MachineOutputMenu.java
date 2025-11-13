package me.theabab2333.headtap.inventory;

import dev.dubhe.anvilcraft.inventory.BaseMachineMenu;
import lombok.Getter;
import me.theabab2333.headtap.block.entity.AbstractBaseMachineBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class MachineOutputMenu extends BaseMachineMenu {
    private final AbstractBaseMachineBlockEntity blockEntity;
    private final Level level;

    protected MachineOutputMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, @NotNull BlockEntity blockEntity) {
        super(menuType, containerId, blockEntity);
        this.blockEntity = (AbstractBaseMachineBlockEntity) blockEntity;
        this.level = inventory.player.level();
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

    public void setEnabled(boolean enabled) {
        if (blockEntity instanceof AbstractBaseMachineBlockEntity entity){
            entity.setEnabled(enabled);
        }
    }
}
