package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import dev.dubhe.anvilcraft.init.ModBlocks;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HyperbaricBlockEntity extends BlockEntity implements IFilterBlockEntity, IItemHandlerHolder {
    public int hardendLevel = 0;
    public boolean headend = true;

    private final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(9) {
        public void onContentsChanged(int slot) {
            HyperbaricBlockEntity.this.setChanged();
        }
    };

    public static final Map<Block, Integer> HAEDEND_BLOCKS = new HashMap<>();

    static {
        HAEDEND_BLOCKS.put(ModBlocks.HEAVY_IRON_BLOCK.get(), 1);
    }

    public boolean toHyperbaric(int count) {
        if (this.level == null || this.level.isClientSide()) return false;
        hardendLevel = 0;
        if (count >= 4) {
            BlockPos thisPos = this.getBlockPos();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos otherPos = thisPos.relative(direction);
                BlockPos otherHaedendPos = new BlockPos(otherPos.getX(), otherPos.getY() - 1, otherPos.getZ());
                BlockState otherHaedendState = level.getBlockState(otherHaedendPos);
                Integer haedendCount = HAEDEND_BLOCKS.get(otherHaedendState.getBlock());
                if (haedendCount != null) {
                    hardendLevel += haedendCount;
                } else headend = false;
            }
        }
        System.out.println(hardendLevel);
        System.out.println(headend);
        return true;
    }

    public Item craft() {
        if (level == null) return Items.AIR;

        return Items.AIR;
    }

    public HyperbaricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        tag.put("Inventory", this.itemHandler.serializeNBT(provider));
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public FilteredItemStackHandler getFilteredItemDepository() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.HYPERBARIC.get(),
            (be, context) -> be.itemHandler
        );
    }
}
