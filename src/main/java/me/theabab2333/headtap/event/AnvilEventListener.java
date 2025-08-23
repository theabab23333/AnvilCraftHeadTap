package me.theabab2333.headtap.event;

import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.block.PiezoelectricCrystalBlock;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.block.AnvilObserverBlock;
import me.theabab2333.headtap.init.ModAnvilBehaviors;
import me.theabab2333.headtap.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber(modid = HeadTap.MOD_ID)
public class AnvilEventListener {
    private static boolean behaviorRegistered = false;
    @SubscribeEvent
    public static void onLand(@NotNull AnvilEvent.OnLand event){
        if (!behaviorRegistered) {
            ModAnvilBehaviors.register();
            PiezoelectricCrystalBlock.ANVIL_TYPES.put(ModBlocks.AMETHYST_ANVIL.get(), List.of(2, 4, 6, 8));
            behaviorRegistered = true;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        handleAnvilObserverBlock(level, pos);
    }

    public static void handleAnvilObserverBlock(Level level, BlockPos pos) {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockPos blockPos = pos.below().relative(d);
            Block block = level.getBlockState(blockPos).getBlock();
            if (block instanceof AnvilObserverBlock anvilObserverBlock) {
                BlockState blockState = level.getBlockState(blockPos);
                Direction oDirection = blockState.getValue(AnvilObserverBlock.FACING).getOpposite();
                if (oDirection != d) continue;
                anvilObserverBlock.hit(blockState, level, blockPos);
            }
        }
    }
}