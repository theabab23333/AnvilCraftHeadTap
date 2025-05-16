package me.theabab2333.head_tap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoneGeneratorBlockEntity extends BlockEntity implements IItemHandlerHolder {

    public StoneGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public Item GetStoneType(){
        //暂时是用一个写定的圆石，后面可以再改
        return Items.COBBLESTONE;
    }

    public boolean TryGenerateStone(Item stoneType, int count) {
        ItemStack item = new ItemStack(stoneType, count);
        ItemStack remaining = ItemHandlerHelper.insertItem(itemHandler, item, false);
        return !remaining.isEmpty();
    }

    public boolean TryGenerateStone(int count) {
        return this.TryGenerateStone(GetStoneType(), count);
    }
}
