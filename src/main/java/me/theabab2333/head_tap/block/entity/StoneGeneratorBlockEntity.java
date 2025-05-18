package me.theabab2333.head_tap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private int behavior = 0;

    public StoneGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
        tag.putInt("Behavior", this.behavior);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
        tag.putInt("Behavior", this.behavior);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    //修改逻辑 判断行为
    //0是有岩浆和水的时候生成圆石 1是没岩浆和水的时候粉碎圆石/沙砾生成沙砾/沙子 剩下的2/3可以以后再想
    public boolean getBehavior(int count) {
        if (behavior == 0){
            return generateStone(count);
        } else {
            return crush(count);
        }
    }

    private boolean generateStone(int count) {
        ItemStack item = new ItemStack(Items.COBBLESTONE, count);
        ItemStack remaining = ItemHandlerHelper.insertItem(itemHandler, item, false);
        return !remaining.isEmpty();
    }

    private boolean crush(int count) {
        return false;
    }

//    public int ChangeBehavior() {
//
//        return behavior = 0;
//    }
//
//    public Item GetStoneType(){
//        //暂时是用一个写定的圆石，后面可以再改
//        return Items.COBBLESTONE;
//    }
//
//    public boolean TryGenerateStone(Item type, int count) {
//        ItemStack item = new ItemStack(type, count);
//        ItemStack remaining = ItemHandlerHelper.insertItem(itemHandler, item, false);
//        return !remaining.isEmpty();
//    }
//
//    public boolean GetBehavior(int count) {
//        if (behavior == 0) {
//            return this.TryGenerateStone(GetStoneType(), count);
//        }
//        return false;
//    }
}
