package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import dev.dubhe.anvilcraft.block.entity.BaseChuteBlockEntity;
import dev.dubhe.anvilcraft.block.entity.SimpleChuteBlockEntity;
import lombok.Getter;
import me.theabab2333.headtap.block.DistributorBlock;
import me.theabab2333.headtap.init.block.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil.getTargetItemHandlerList;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DistributorBlockEntity extends BlockEntity implements IItemHandlerHolder {
    @Getter
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private int cooldown = 0;
    private boolean unbalance = true;
    private long tickedGameTime;

    public DistributorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    //以下很多都是直接抄的简易溜槽，就tick和tryFill是搞得比较新的

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("Cooldown", cooldown);
        tag.putBoolean("Unbalance", unbalance);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        cooldown = tag.getInt("Cooldown");
        unbalance = tag.getBoolean("Unbalance");
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    @SuppressWarnings({"UnreachableCode", "DuplicatedCode"})
    public void tick() {
        Level lvl = this.getLevel();
        if (lvl == null || lvl.isClientSide()) return;
        if (cooldown > 0) cooldown--;
        tickedGameTime = lvl.getGameTime();
        if (cooldown == 0 && !this.itemHandler.getStackInSlot(0).isEmpty()) {
            cooldown = AnvilCraft.CONFIG.chuteMaxCooldown + 1;
        }
        if (cooldown == 1) {
            BlockPos targetPos = getBlockPos().relative(getOutputDirection());
            BlockPos targetPos2 = getBlockPos().relative(getOutputDirection().getOpposite());
            BlockEntity targetBE = lvl.getBlockEntity(targetPos);
            BlockEntity targetBE2 = lvl.getBlockEntity(targetPos2);

            //什么情况会吐东西呢？
            //两面都是可以吐东西的方块（即不是非容器实心方块）
            boolean canSpit = false;

            List<IItemHandler> targetList = getTargetItemHandlerList(targetPos, getOutputDirection().getOpposite(), lvl);
            List<IItemHandler> targetList2 = getTargetItemHandlerList(targetPos2, getOutputDirection(), lvl);
            Vec3 center = getBlockPos().relative(getDirection()).getCenter();
            Vec3 center2 = getBlockPos().relative(getDirection().getOpposite()).getCenter();
            AABB aabb = new AABB(center.add(-0.125, -0.125, -0.125), center.add(0.125, 0.125, 0.125));
            AABB aabb2 = new AABB(center2.add(-0.125, -0.125, -0.125), center2.add(0.125, 0.125, 0.125));
            if (
                (!targetList.isEmpty() || getLevel().noCollision(aabb)) &&
                    (!targetList2.isEmpty() || getLevel().noCollision(aabb2))
            ) canSpit = true;

            if (!canSpit) return;

            for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                ItemStack stack = this.itemHandler.getStackInSlot(i);
                if (stack.isEmpty() || stack.getCount() == 0) {
                    continue;
                }
                int zx = stack.getCount();
                int xx = Math.ceilDiv(zx, 2);
                zx -= xx;
                if (xx > zx) {
                    if (!unbalance) unbalance = true;
                    else {
                        zx += 1; xx -= 1;
                        unbalance = false;
                    }
                }

                tryFill(xx, i, targetPos, targetBE);
                tryFill(zx, i, targetPos2, targetBE2);
                break;
            }
        }
        lvl.updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
    }

    private void tryFill(int count, int slotIndex, BlockPos targetPos, @Nullable BlockEntity targetBE) {
        Level lvl = this.getLevel();
        if (lvl == null || lvl.isClientSide()) return;
        boolean isTargetEmpty = false;
        if (targetBE != null) isTargetEmpty = isTargetEmpty(targetBE);
        // 尝试向朝向容器输出
        List<IItemHandler> targetList = getTargetItemHandlerList(targetPos, getOutputDirection().getOpposite(), level);
        if (targetList != null && !targetList.isEmpty()) {
            for (IItemHandler target : targetList) {
                boolean success = ItemHandlerUtil.exportToTarget(getItemHandler(), count, s -> true, target);
                if (success) {
                    if (isTargetEmpty) setChuteCD(targetBE);
                    break;
                }
            }
        } else {
            Vec3 center = targetPos.getCenter();
            AABB aabb = new AABB(center.add(-0.125, -0.125, -0.125), center.add(0.125, 0.125, 0.125));
            if (lvl.noCollision(aabb)) {
                ItemStack stack = this.itemHandler.getStackInSlot(slotIndex).copy();
                ItemStack droppedItemStack = stack.copy();
                droppedItemStack.setCount(count);
                stack.shrink(count);
                if (stack.getCount() == 0) stack = ItemStack.EMPTY;
                ItemEntity itemEntity = new ItemEntity(lvl, center.x, center.y, center.z, droppedItemStack, 0, 0, 0);
                itemEntity.setDefaultPickUpDelay();
                lvl.addFreshEntity(itemEntity);
                this.itemHandler.setStackInSlot(slotIndex, stack);
            }
        }
    }

    public boolean isTargetEmpty(BlockEntity blockEntity) {
        if (blockEntity instanceof SimpleChuteBlockEntity chute) {
            return chute.isEmpty();
        }
        if (blockEntity instanceof BaseChuteBlockEntity chute) {
            return chute.isEmpty();
        }
        return false;
    }

    private void setChuteCD(BlockEntity targetBE) {
        if (targetBE instanceof BaseChuteBlockEntity chute) {
            int k = 0;
            if (chute.getTickedGameTime() >= this.tickedGameTime) k++;
            chute.setCooldown(AnvilCraft.CONFIG.chuteMaxCooldown - k);
        }
        if (targetBE instanceof SimpleChuteBlockEntity chute) {
            int k = 0;
            if (chute.getTickedGameTime() >= this.tickedGameTime) k++;
            chute.setCooldown(AnvilCraft.CONFIG.chuteMaxCooldown - k);
        }
    }

    private Direction getDirection() {
        if (getLevel() == null) return Direction.NORTH;
        BlockState state = getLevel().getBlockState(getBlockPos());
        if (state.getBlock() instanceof DistributorBlock) {
            return state.getValue(DistributorBlock.FACING);
        }
        return Direction.NORTH;
    }

    /**
     * @return 红石信号强度
     */
    public int getRedstoneSignal() {
        int i = 0;
        for (int j = 0; j < itemHandler.getSlots(); ++j) {
            ItemStack itemStack = itemHandler.getStackInSlot(j);
            if (itemStack.isEmpty()) {
                continue;
            }
            ++i;
        }
        return i;
    }

    protected Direction getOutputDirection() {
        return getDirection();
    }

    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.DISTRIBUTER.get(),
            (be, context) -> be.getItemHandler()
        );
    }

}
