package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import me.theabab2333.headtap.init.ModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PassiveRoyalSmithingTableBlockEntity extends BlockEntity implements IItemHandlerHolder, IFilterBlockEntity {

    public final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(4) {

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == 0) {
                return super.insertItem(0, stack, simulate);
            } else if (slot == 1) {
                return super.insertItem(1, stack, simulate);
            } else if (slot == 2){
                return super.insertItem(2, stack, simulate);
            } else {
                return stack;
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == 3 ? super.extractItem(3, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (level == null) return false;
            final List<RecipeHolder<SmithingRecipe>> recipes;
            recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING);
            if (slot == 0) {
                return recipes.stream().anyMatch(smithingRecipeRecipeHolder ->
                    smithingRecipeRecipeHolder.value().isTemplateIngredient(stack));
            } else if (slot == 1) {
                return recipes.stream().anyMatch(smithingRecipeRecipeHolder ->
                    smithingRecipeRecipeHolder.value().isBaseIngredient(stack));
            } else if (slot == 2) {
                return recipes.stream().anyMatch(smithingRecipeRecipeHolder ->
                    smithingRecipeRecipeHolder.value().isAdditionIngredient(stack));
            }
            return true;
        }
    };

    public void createResult() {
        if (level == null) return;
        SmithingRecipeInput smithingrecipeinput = createRecipeInput();
        List<RecipeHolder<SmithingRecipe>> list =
            level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, smithingrecipeinput, level);
        if (!list.isEmpty()) {
            RecipeHolder<SmithingRecipe> recipeholder = list.getFirst();
            ItemStack itemstack = recipeholder.value().assemble(smithingrecipeinput, level.registryAccess());
            if (itemstack.isItemEnabled(level.enabledFeatures()) && itemHandler.getStackInSlot(3).isEmpty()) {
                if (itemHandler.getStackInSlot(3).isEmpty()) {
                    itemHandler.setStackInSlot(3, itemstack);
                    clearSlot();
                } else {
                    if (itemstack.getItem() == itemHandler.getStackInSlot(3).getItem()) {
                        itemHandler.getStackInSlot(3).grow(itemstack.getCount());
                        clearSlot();
                    }
                }
            }
        }
    }

    private void clearSlot() {
        for (int i = 1; i < 3; i++) {
            itemHandler.getStackInSlot(i).shrink(1);
        }
    }

    private SmithingRecipeInput createRecipeInput() {
        return new SmithingRecipeInput(
            itemHandler.getStackInSlot(0),
            itemHandler.getStackInSlot(1),
            itemHandler.getStackInSlot(2));
    }

    public PassiveRoyalSmithingTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public FilteredItemStackHandler getFilteredItemDepository() {
        return itemHandler;
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.PASSIVE_ROYAL_TABLE.get(),
            (be, context) -> be.itemHandler
        );
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inv", itemHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Inv"));
    }
}
