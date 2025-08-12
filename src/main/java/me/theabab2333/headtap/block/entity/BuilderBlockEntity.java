package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.BaseMachineBlockEntity;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockConversionRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;
import lombok.Getter;
import me.theabab2333.headtap.block.BuilderBlock;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModMenuTypes;
import me.theabab2333.headtap.inventory.BuilderMenu;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BuilderBlockEntity extends BaseMachineBlockEntity implements IFilterBlockEntity, IItemHandlerHolder {
    private final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            assert level != null;
            List<ItemStack> itemStackList = getIngredientList();
            return itemStackList.stream().anyMatch(itemStack -> itemStack.is(stack.getItem()));
        }
    };

    private List<ItemStack> getIngredientList() {
        assert level != null;
        List<ItemStack> ingredientList = new ArrayList<>();
        List<RecipeHolder<MultiblockRecipe>> multiblockRecipe;
        multiblockRecipe =
            level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MULTIBLOCK_TYPE.get());
        List<RecipeHolder<MultiblockConversionRecipe>> multiblockConversionRecipe;
        multiblockConversionRecipe =
            level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MULTIBLOCK_CONVERSION_TYPE.get());

        // idea建议我换的增强for 我觉得fori挺好
        for (RecipeHolder<MultiblockRecipe> multiblockRecipeRecipeHolder : multiblockRecipe) {
            ingredientList.addAll(multiblockRecipeRecipeHolder.value()
                .getPattern().toIngredientList());
        }
        for (RecipeHolder<MultiblockConversionRecipe> multiblockConversionRecipeRecipeHolder :multiblockConversionRecipe) {
            ingredientList.addAll(multiblockConversionRecipeRecipeHolder.value()
                .getOutputPattern().toIngredientList());
        }

        return ingredientList;
    }

    public BuilderBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public Direction getDirection() {
        assert this.level != null;
        BlockState state = this.level.getBlockState(this.getBlockPos());
        if (state.is(ModBlocks.BUILDER.get())) return state.getValue(BuilderBlock.FACING);
        return Direction.NORTH;
    }

    @Override
    public void setDirection(Direction direction) {
        assert getLevel() != null;
        BlockState state = getLevel().getBlockState(getBlockPos());
        assert state.is(ModBlocks.BUILDER.get());
        getLevel().setBlockAndUpdate(getBlockPos(), state.setValue(BuilderBlock.FACING, direction));
    }

    @Override
    public FilteredItemStackHandler getFilteredItemDepository() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.BUILDER.get(),
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

    @Override
    public Component getDisplayName() {
        return Component.translatable("test");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        assert !player.isSpectator();
        return new BuilderMenu(ModMenuTypes.BUILDER.get(), i, inventory, this);
    }
}
