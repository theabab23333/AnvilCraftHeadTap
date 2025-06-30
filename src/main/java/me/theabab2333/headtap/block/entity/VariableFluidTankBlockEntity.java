package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerComponentType;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.block.state.DirectionCube3x3PartHalf;
import me.theabab2333.headtap.block.VariableFluidTankBlock;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VariableFluidTankBlockEntity extends BlockEntity implements IPowerConsumer {
    private PowerGrid grid;
    public FluidTank tank = new FluidTank(4000) {
        protected void onContentsChanged() {
            setChanged();
        }
    };

    public VariableFluidTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.VARIABLE_FLUID_TANK.get(), pos, blockState);
    }

    private VariableFluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static VariableFluidTankBlockEntity createBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return new VariableFluidTankBlockEntity(type, pos, state);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tank.writeToNBT(provider, tag);
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        tank.readFromNBT(provider, tag);
    }

    public IFluidHandler getFluidHandler() {
        return tank;
    }

    @Override
    @NotNull
    public PowerComponentType getComponentType() {
        if (level == null) return PowerComponentType.INVALID;
        if (!level.getBlockState(getBlockPos()).hasProperty(VariableFluidTankBlock.HALF)) return PowerComponentType.INVALID;
        if (level.getBlockState(getBlockPos()).getValue(VariableFluidTankBlock.HALF).equals(DirectionCube3x3PartHalf.MID_CENTER)) return PowerComponentType.CONSUMER;
        else return PowerComponentType.INVALID;
    }

    public boolean isWork() {
        BlockState state = getBlockState();
        return state.getValue(VariableFluidTankBlock.SWITCH) == Switch.ON && !state.getValue(VariableFluidTankBlock.OVERLOAD);
    }

    public void tick() {
        if (level == null) return;
        BlockState state = getBlockState();
        if (level.isClientSide) {
            if (!state.getValue(VariableFluidTankBlock.HALF).equals(DirectionCube3x3PartHalf.MID_CENTER)) return;
            // 这里先空着 之后可能会用
            if (isWork()) saveFluid();
        }
        if (grid == null) return;
        if (!state.getValue(VariableFluidTankBlock.HALF).equals(DirectionCube3x3PartHalf.MID_CENTER)) return;
        if (!(state.getBlock() instanceof VariableFluidTankBlock block)) return;
        if (grid.isWorking() && state.getValue(VariableFluidTankBlock.OVERLOAD)) {
            block.updateState(level, getBlockPos(), VariableFluidTankBlock.OVERLOAD, false, 3);
        } else if (!grid.isWorking() && !state.getValue(VariableFluidTankBlock.OVERLOAD)) {
            block.updateState(level, getBlockPos(), VariableFluidTankBlock.OVERLOAD, true, 3);
        }
        if (!isWork()) return;
        if (state.getValue(VariableFluidTankBlock.FACING).equals(Direction.UP)) heaterFluids();
    }

    public void saveFluid() {

    }

    public void heaterFluids() {

    }

    @Override
    public Level getCurrentLevel() {
        return level;
    }

    @Override
    @NotNull
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public int getInputPower() {
        return getBlockState().getValue(VariableFluidTankBlock.SWITCH) == Switch.ON ? 256 : 0;
    }

    @Override
    public int getRange() {
        return 1;
    }

    @Override
    public void setGrid(@Nullable PowerGrid grid) {
        this.grid = grid;
    }

    @Override
    public @Nullable PowerGrid getGrid() {
        return this.grid;
    }
}
