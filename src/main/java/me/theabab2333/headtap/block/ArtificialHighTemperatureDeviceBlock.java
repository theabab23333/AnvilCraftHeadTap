package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import me.theabab2333.headtap.block.entity.ArtificialHighTemperatureDeviceBlockEntity;
import me.theabab2333.headtap.init.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ArtificialHighTemperatureDeviceBlock extends AbstractBaseMachineBlock {
    public ArtificialHighTemperatureDeviceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ArtificialHighTemperatureDeviceBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArtificialHighTemperatureDeviceBlockEntity(ModBlockEntities.ARTIFICIAL_HIGH_TEMPERATURE_DEVICE.get(), blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(
            type,
            ModBlockEntities.ARTIFICIAL_HIGH_TEMPERATURE_DEVICE.get(),
            (level1, blockPos, blockState, blockEntity) -> blockEntity.tick()
        );
    }
}
