package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import me.theabab2333.headtap.block.entity.JadeWorldBlockEntity;
import me.theabab2333.headtap.init.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JadeWorldBlock extends BetterBaseEntityBlock {

    public JadeWorldBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(JadeWorldBlock::new);
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new JadeWorldBlockEntity(ModBlockEntities.JADE_WORLD.get(), blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
//        BlockEntity blockEntity = level.getBlockEntity(pos);
//        if (player instanceof ServerPlayer serverPlayer) {
//            if (level instanceof ServerLevel serverLevel) {
//                if (blockEntity instanceof JadeWorldBlockEntity be) {
//                    if (be.isRevisit()) {
//                        // TODO: 创建私人维度和传送到世界
//
//                        ResourceKey<Level> targetDimension = be.getTargetDimension();
//                        MinecraftServer minecraftServer = serverLevel.getServer();
//                        ServerLevel targetLevel = minecraftServer.getLevel(targetDimension);
//
//                        LevelStem levelStem = serverLevel.registryAccess()
//                            .lookupOrThrow(Registries.LEVEL_STEM)
//                            .getOrThrow(Registries.levelToLevelStem(targetDimension))
//                            .value();
//
//                        if (targetLevel != null) {
//
//                        } else {
//
//                        }
//                    }
//                }
//            }
//        }
        return InteractionResult.SUCCESS;
    }
}
