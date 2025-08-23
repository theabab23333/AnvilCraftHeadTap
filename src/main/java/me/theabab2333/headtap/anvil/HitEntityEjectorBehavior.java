package me.theabab2333.headtap.anvil;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import me.theabab2333.headtap.block.EntityEjectorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HitEntityEjectorBehavior implements IAnvilBehavior {

    @Override
    public boolean handle(Level level, BlockPos hitBlockPos, BlockState hitBlockState, float fallDistance, AnvilEvent.OnLand event) {
        Block block = hitBlockState.getBlock();
        if (block instanceof EntityEjectorBlock entityEjectorBlock) {
            int count = (int) fallDistance + 1;
            count = count <= 0 ? 1 : count;
            entityEjectorBlock.getHigh(count, level, hitBlockPos);
        }
        return false;
    }
}
