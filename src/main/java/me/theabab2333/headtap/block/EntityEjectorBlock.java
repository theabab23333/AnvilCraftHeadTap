package me.theabab2333.headtap.block;

import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import me.theabab2333.headtap.init.ModBlockStateProperties;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.recipe.EjectorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class EntityEjectorBlock extends Block implements IHammerRemovable {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty HIGH = ModBlockStateProperties.HIGH;

    public EntityEjectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition
            .any()
            .setValue(POWERED, false)
            .setValue(HIGH, 0));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, HIGH);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()))
            .setValue(HIGH, 0);
    }

    @Override
    public void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block neighborBlock,
        BlockPos neighborPos,
        boolean movedByPiston
    ) {
        if (level.isClientSide) {
            return;
        }
        boolean bl = state.getValue(POWERED);
        if (bl != level.hasNeighborSignal(pos)) {
            if (bl) {
                level.scheduleTick(pos, this, 4);
            } else {
                level.setBlockAndUpdate(pos, state.cycle(POWERED));
            }
        }
    }

    @Override
    public void tick(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        RandomSource random) {
        if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
            level.setBlockAndUpdate(pos, state.cycle(POWERED));
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        int high = state.getValue(HIGH);
        if (state.getValue(POWERED) && high > 0) {
            level.setBlockAndUpdate(pos, state.setValue(HIGH, 0));
            entity.move(MoverType.PLAYER, Vec3.ZERO.relative(Direction.UP, high));

            if (entity instanceof ItemEntity itemEntity) {
                ItemStack itemStack = itemEntity.getItem();
                int needHigh = getRecipeHigh(itemStack, level);

                for (int i = 1; i < needHigh + 1; i++) {
                    BlockPos upPos = pos.above(i);
                    if (!level.getBlockState(upPos).isAir()) return;
                }

                EjectorRecipe recipe = getItemRecipe(itemStack, level);
                if (high >= needHigh && recipe != null) {
                    ItemStack results = recipe.result;
                    Item result = results.getItem();
                    int count = itemStack.getCount();
                    itemEntity.setItem(new ItemStack(result, count));
                }
            }
        }
    }

    private EjectorRecipe getItemRecipe(ItemStack itemStack, Level level) {
        SingleRecipeInput input = new SingleRecipeInput(itemStack);
        Optional<RecipeHolder<EjectorRecipe>> x = level.getRecipeManager().getRecipeFor(ModRecipeTypes.EJECTOR.get(), input, level);
        return x.map(RecipeHolder::value).orElse(null);
    }

    private int getRecipeHigh(ItemStack itemStack, Level level) {
        SingleRecipeInput input = new SingleRecipeInput(itemStack);
        Optional<RecipeHolder<EjectorRecipe>> x = level.getRecipeManager().getRecipeFor(ModRecipeTypes.EJECTOR.get(), input, level);
        if (x.isPresent()) {
            if (x.get().value().high == 0) return 0;
            return x.get().value().high;
        }
        return 0;
    }

    public void getHigh(int count, Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        int high = count + blockState.getValue(HIGH);
        high = high <= 0 ? 1 :  Math.min(high, 128);
        level.setBlockAndUpdate(blockPos, blockState.setValue(HIGH, high));
    }
}
