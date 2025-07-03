package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DensityCoreBlock extends Block {
    public DensityCoreBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<DensityCoreBlock> CODEC = simpleCodec(DensityCoreBlock::new);

    public static final VoxelShape SHAPE = box(1, 1, 1, 15, 15, 15);

    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    protected @NotNull VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context) {
        return SHAPE;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}
