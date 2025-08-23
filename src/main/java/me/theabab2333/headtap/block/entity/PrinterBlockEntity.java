package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import lombok.Getter;
import me.theabab2333.headtap.api.GetEnchantments;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrinterBlockEntity extends BlockEntity implements IFilterBlockEntity {
    @Getter
    private ItemStack displayItemStack = null;
    private final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(4) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot != 3) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return stack;
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == 3 ? super.extractItem(3, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return stack.is(ModItems.BLESSED_GOLD_INGOT);
            } else if (slot == 1) {
                return stack.is(dev.dubhe.anvilcraft.init.ModItems.CURSED_GOLD_INGOT);
            } else if (slot == 2) {
                return stack.is(Items.BOOK);
            } else return true;
        }
    };
    private int bCount = 0;
    private int cCount = 0;

    public PrinterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.PRINTER.get(),
            (be, context) -> be.itemHandler
        );
    }

    public void getPrintBook(ItemStack itemStack) {
        if (displayItemStack == itemStack) return;
        displayItemStack = itemStack;
        bCount = GetEnchantments.calculateTheSumOfOther(displayItemStack) * 4;
        cCount = GetEnchantments.calculateTheSumOfAllCruse(displayItemStack) * 4;
    }

    public void toPrint() {
        assert level != null;
        if (check()) {
            itemHandler.getStackInSlot(0).shrink(getNeedB());
            itemHandler.getStackInSlot(1).shrink(getNeedC());
            itemHandler.getStackInSlot(2).shrink(1);
            itemHandler.setStackInSlot(3, displayItemStack.copy());
        }
    }

    private boolean check() {
        assert level != null;
        if (displayItemStack != null && displayItemStack == getItemStack()) {
            return itemHandler.getStackInSlot(0).getCount() >= bCount &&
                itemHandler.getStackInSlot(1).getCount() >= cCount &&
                itemHandler.getStackInSlot(2).getCount() > 0 &&
                itemHandler.getStackInSlot(3).isEmpty();
        }
        return false;
    }

    private ItemStack getItemStack() {
        assert level != null;
        List<ItemEntity> entities = itemEntities(getAABB());
        assert !entities.isEmpty();
        for (ItemEntity entity : entities) {
            if (entity.getItem().getItem() instanceof EnchantedBookItem) {
                if (displayItemStack == entity.getItem()) return entity.getItem();
            }
        }
        return ItemStack.EMPTY;
    }

    private AABB getAABB() {
        return new AABB(getBlockPos().above());
    }

    private List<ItemEntity> itemEntities(AABB aabb) {
        assert level != null;
        return level.getEntities(EntityTypeTest.forClass(ItemEntity.class), aabb, Entity::isAlive);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getNeedB() {
        return bCount;
    }

    public int getNeedC() {
        return cCount;
    }

    @Override
    public FilteredItemStackHandler getFilteredItemStackHandler() {
        return itemHandler;
    }
}
