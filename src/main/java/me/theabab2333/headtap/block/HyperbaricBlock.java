package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import me.theabab2333.headtap.block.entity.HyperbaricBlockEntity;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HyperbaricBlock extends BetterBaseEntityBlock implements IHammerRemovable {

    public HyperbaricBlock(Properties pProperties) {
        super(pProperties);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(HyperbaricBlock::new);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HyperbaricBlockEntity(ModBlockEntities.HYPERBARIC.get(), blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
