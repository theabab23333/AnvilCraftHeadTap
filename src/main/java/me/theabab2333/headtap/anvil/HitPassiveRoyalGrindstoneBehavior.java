package me.theabab2333.headtap.anvil;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import me.theabab2333.headtap.block.entity.PassiveRoyalGrindstoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HitPassiveRoyalGrindstoneBehavior implements IAnvilBehavior {

    @Override
    public boolean handle(Level level, BlockPos hitBlockPos, BlockState hitBlockState, float fallDistance, AnvilEvent.OnLand event) {
        if (!hitBlockState.hasBlockEntity()) return false;
        PassiveRoyalGrindstoneBlockEntity blockEntity = (PassiveRoyalGrindstoneBlockEntity) level.getBlockEntity(hitBlockPos);
        if (blockEntity == null) return false;
        blockEntity.createResult();
        return false;
    }
}
