package me.theabab2333.headtap.block.entity;

import dev.dubhe.anvilcraft.api.IHasDisplayItem;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.api.itemhandler.IItemHandlerHolder;
import dev.dubhe.anvilcraft.block.entity.BaseMachineBlockEntity;
import dev.dubhe.anvilcraft.block.entity.IFilterBlockEntity;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPattern;
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
import net.minecraft.world.Containers;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BuilderBlockEntity extends BaseMachineBlockEntity implements IFilterBlockEntity, IItemHandlerHolder, IHasDisplayItem {
    // 来自本体MultiBlockCraftingCategory和BatchCrafter
    // 妈的 屎一样 能跑就行(

    private final FilteredItemStackHandler itemHandler = new FilteredItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            assert level != null;
            List<ItemStack> itemStackList = getIngredientList();
            return itemStackList.stream().anyMatch(itemStack -> itemStack.is(stack.getItem()));
        }
    };
    private ItemStack displayItemStack = ItemStack.EMPTY;
    private static final Comparator<ItemStack> BY_COUNT_DECREASING =Comparator.comparing(ItemStack::getCount)
        .thenComparing(ItemStack::getDescriptionId).reversed();

    public List<RecipeHolder<MultiblockRecipe>> recipeHolderList() {
        assert level != null;
        return level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MULTIBLOCK_TYPE.get());
    }

    private List<ItemStack> getIngredientList() {
        assert level != null;
        List<ItemStack> ingredientList = new ArrayList<>();
        for (RecipeHolder<MultiblockRecipe> multiblockRecipeRecipeHolder : recipeHolderList()) {
            ingredientList.addAll(
                multiblockRecipeRecipeHolder.value().getPattern().toIngredientList());
        }
        return ingredientList;
    }

    private List<ItemStack> getResultList() {
        List<ItemStack> resultList = new ArrayList<>();
        for (RecipeHolder<MultiblockRecipe> multiblockRecipeRecipeHolder : recipeHolderList()) {
            resultList.add(multiblockRecipeRecipeHolder.value().getResult());
        }
        return resultList;
    }

    public void tick() {
        assert level != null;
        List<ItemFrame> itemFrames = level.getEntitiesOfClass(ItemFrame.class, new AABB(getBlockPos().above()));
        if (!itemFrames.isEmpty()) {
            ItemFrame itemFrame = itemFrames.getFirst();
            ItemStack itemStack = itemFrame.getItem();
            if (itemStack.isEmpty()) {
                itemHandler.setFilterEnabled(false);
                displayItemStack = ItemStack.EMPTY;
                return;
            }
            if (displayItemStack.getItem() != itemStack.getItem()) checkDisplayItemStack(itemStack);

            BlockState state = level.getBlockState(getBlockPos());
            level.updateNeighbourForOutputSignal(getBlockPos(), state.getBlock());
            boolean powered = state.getValue(BuilderBlock.POWERED);
            if (powered && !level.isClientSide) toBuild();
        }
    }

    private void setFilter() {
        assert level != null;
        if (displayItemStack.isEmpty()) return;
        for (RecipeHolder<MultiblockRecipe> multiblockRecipeRecipeHolder : recipeHolderList()) {
            List<ItemStack> itemStackList = multiblockRecipeRecipeHolder.value().getPattern().toIngredientList();
            Containers.dropContents(level, getBlockPos().above(), itemHandler.getStacks());
            if (multiblockRecipeRecipeHolder.value().getResult().is(displayItemStack.getItem())) {
                itemStackList.sort(BY_COUNT_DECREASING);
                itemHandler.setFilterEnabled(false);
                itemHandler.setFilterEnabled(true);
                for (int i = 0; i < itemStackList.size(); i++) {
                    ItemStack filter = itemStackList.get(i);
                    itemHandler.setFilter(i, filter);
                }
                break;
            }
        }
    }

    private boolean checkFilter() {
        assert level != null;
        if (displayItemStack.isEmpty()) return false;
        for (RecipeHolder<MultiblockRecipe> multiblockRecipeRecipeHolder : recipeHolderList()) {
            if (multiblockRecipeRecipeHolder.value().getResult().is(displayItemStack.getItem())) {
                List<ItemStack> itemStackList = multiblockRecipeRecipeHolder.value().getPattern().toIngredientList();
                itemStackList.sort(BY_COUNT_DECREASING);
                for (int i = 0; i < itemStackList.size(); i++) {
                    ItemStack needItemStack = itemStackList.get(i);
                    ItemStack nowItemStack = itemHandler.getStackInSlot(i);
                    if (needItemStack.is(nowItemStack.getItem())) return true;
                }
                return false;
            }
        }
        return false;
    }

    private void toBuild() {
        assert level!=null;
        if (checkFilter()) {
            for (RecipeHolder<MultiblockRecipe> recipe : recipeHolderList()) {
                if (recipe.value().getResult().is(displayItemStack.getItem())) {
                    Direction direction = getBlockState().getValue(BuilderBlock.FACING);
                    BlockPos blockPos = getBlockPos().relative(direction, 2);
                    BlockPos centerPos = blockPos.above();
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            for (int k = -1; k <= 1; k++) {
                                BlockPos bp = new BlockPos(centerPos.getX() + i, centerPos.getY() + j, centerPos.getZ() + k);
                                BlockPattern blockPattern = recipe.value().getPattern();
                                BlockState blockState = level.getBlockState(bp);
                                BlockState readyState = blockPattern.getPredicate(i + 1, j + 1, k + 1).getDefaultState();
                                if (blockState.isAir() || blockState == readyState) {
                                    level.setBlockAndUpdate(bp, readyState);
                                } else return;
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    private void checkDisplayItemStack(ItemStack itemFrame) {
        List<ItemStack> itemStackList = getResultList();
        if (itemStackList.stream().anyMatch(itemStack -> itemStack.is(itemFrame.getItem()))) {
            displayItemStack = itemFrame;
            setFilter();
        }
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
        if (!displayItemStack.isEmpty()) {
            CompoundTag item = (CompoundTag) this.displayItemStack.save(provider);
            tag.put("DisplayItemStack", item);
        }
        tag.put("Inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        CompoundTag ct = tag.getCompound("DisplayItemStack");
        displayItemStack = ct.contains("id") ? ItemStack.parse(provider, ct).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
        itemHandler.deserializeNBT(provider, tag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.headtap.builder.top");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        assert !player.isSpectator();
        return new BuilderMenu(ModMenuTypes.BUILDER.get(), i, inventory, this);
    }

    @Override
    public void updateDisplayItem(ItemStack stack) {
        displayItemStack = stack;
    }
}
