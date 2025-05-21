package me.theabab2333.head_tap.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.api.event.anvil.AnvilFallOnLandEvent;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import me.theabab2333.head_tap.block.entity.ExcavatorBlockEntity;
import me.theabab2333.head_tap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExcavatorBlock extends BetterBaseEntityBlock {

    public ExcavatorBlock(Properties properties) {
        super(properties);
    }

    protected MapCodec<? extends BetterBaseEntityBlock> codec() {
        return simpleCodec(ExcavatorBlock::new);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ExcavatorBlockEntity(ModBlockEntities.EXCAVATOR.get(), blockPos, blockState);
    }

    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!level.isClientSide()) {
            ExcavatorBlockEntity blockEntity = (ExcavatorBlockEntity) level.getBlockEntity(pos);
            if (blockEntity != null) {
                return InteractionResult.sidedSuccess(ExcavatorBlockEntity.setValue(1));
            }
        }
        return InteractionResult.SUCCESS;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
