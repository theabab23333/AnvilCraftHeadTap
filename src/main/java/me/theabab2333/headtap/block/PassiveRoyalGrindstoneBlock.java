package me.theabab2333.headtap.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import me.theabab2333.headtap.block.entity.PassiveRoyalGrindstoneBlockEntity;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PassiveRoyalGrindstoneBlock extends BetterBaseEntityBlock implements IHammerRemovable {
    public PassiveRoyalGrindstoneBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PassiveRoyalGrindstoneBlock::new);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PassiveRoyalGrindstoneBlockEntity(ModBlockEntities.PASSIVE_ROYAL_GRINDSTONE.get(), blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(
        BlockState state,
        Level level,
        BlockPos pos,
        BlockState newState,
        boolean movedByPiston) {
        if (state.is(newState.getBlock())) return;
        if (level.getBlockEntity(pos) instanceof PassiveRoyalGrindstoneBlockEntity entity) {
            Vec3 vec3 = entity.getBlockPos().getCenter();
            FilteredItemStackHandler depository = entity.getFilteredItemDepository();
            for (int slot = 0; slot < depository.getSlots(); slot++) {
                Containers.dropItemStack(level, vec3.x, vec3.y, vec3.z, depository.getStackInSlot(slot));
            }
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
