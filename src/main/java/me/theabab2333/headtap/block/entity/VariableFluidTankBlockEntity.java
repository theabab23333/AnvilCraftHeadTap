package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerComponentType;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import me.theabab2333.headtap.block.VariableFluidTankBlock;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VariableFluidTankBlockEntity extends BlockEntity implements IPowerConsumer {
    private PowerGrid grid;

    public VariableFluidTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.VARIABLE_FLUID_TANK.get(), pos, blockState);
    }

    private VariableFluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static VariableFluidTankBlockEntity createBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        return new VariableFluidTankBlockEntity(type, pos, blockState);
    }

    @Override
    public Level getCurrentLevel() {
        return level;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    @NotNull
    public PowerComponentType getComponentType() {
        if (level == null) return PowerComponentType.INVALID;
        if (!level.getBlockState(getBlockPos()).hasProperty(VariableFluidTankBlock.HALF)) return PowerComponentType.INVALID;
        level.getBlockState(getBlockPos()).getValue(VariableFluidTankBlock.HALF);
        return PowerComponentType.INVALID;
    }

    @Override
    public int getRange() {
        return 1;
    }

    @Override
    public int getInputPower() {
        return getBlockState().getValue(VariableFluidTankBlock.SWITCH) == Switch.ON ? 256 : 0;
    }

    @Override
    public void setGrid(@Nullable PowerGrid grid) {
        this.grid = grid;
    }

    @Override
    public @Nullable PowerGrid getGrid() {
        return this.grid;
    }

    public boolean isWork() {
        BlockState state = getBlockState();
        return state.getValue(VariableFluidTankBlock.SWITCH) == Switch.ON;
    }
}
