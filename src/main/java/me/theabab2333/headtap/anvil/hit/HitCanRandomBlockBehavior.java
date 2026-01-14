package me.theabab2333.headtap.anvil.hit;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import me.theabab2333.headtap.init.block.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HitCanRandomBlockBehavior implements IAnvilBehavior {
    @Override
    public boolean handle(
        Level level,
        BlockPos hitBlockPos,
        BlockState hitBlockState,
        float fallDistance,
        AnvilEvent.OnLand event
    ) {
        RandomSource source = level.getRandom();
        ServerLevel serverLevel = (ServerLevel) level;

        for (var direction : Direction.values()) {
            BlockPos blockPos = hitBlockPos.relative(direction);
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.is(ModBlockTags.CAN_HIT_RANDOM)) continue;
            apply(serverLevel, blockState, blockPos, source);
        }

        apply(serverLevel, hitBlockState, hitBlockPos, source);
        return false;
    }

    private void apply(
        ServerLevel level,
        BlockState blockState,
        BlockPos blockPos,
        RandomSource source
    ) {

        for (int i = 0; i < 10; i++) {
            blockState.randomTick(level, blockPos, source);
        }
    }
}