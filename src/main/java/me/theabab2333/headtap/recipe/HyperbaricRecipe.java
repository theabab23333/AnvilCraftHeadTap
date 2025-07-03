package me.theabab2333.headtap.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractRecipeBuilder;
import dev.dubhe.anvilcraft.util.RecipeUtil;
import me.theabab2333.headtap.init.ModRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HyperbaricRecipe implements Recipe<HyperbaricRecipe.Input> {

    // 我抄本体过筛的(cv大法)

    public final Ingredient input;
    public final ItemStack result;
    public final NumberProvider resultAmount;

    public HyperbaricRecipe(Ingredient input, ItemStack result, NumberProvider resultAmount) {
        this.input = input;
        this.result = result;
        this.resultAmount = resultAmount;
    }

    public HyperbaricRecipe(Ingredient input, ItemStack result) {
        this(input, result, ConstantValue.exactly(1));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.HYPERBARIC_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.HYPERBARIC_SERIALIZER.get();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return result;
    }

    @Override
    public boolean matches(Input pInput, Level pLevel) {
        return input.test(pInput.itemStack);
    }

    @Override
    public ItemStack assemble(Input pInput, HolderLookup.Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    public record Input(ItemStack itemStack) implements RecipeInput {
        @Override
        public ItemStack getItem(int pIndex) {
            return itemStack;
        }

        @Override
        public int size() {
            return 1;
        }
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<HyperbaricRecipe> {
        private static final MapCodec<HyperbaricRecipe> CODEC = RecordCodecBuilder.mapCodec(ins ->
            ins.group(Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(HyperbaricRecipe::getInput),
                ItemStack.CODEC.fieldOf("result").forGetter(HyperbaricRecipe::getResult),
                NumberProviders.CODEC.fieldOf("result_amount").forGetter(HyperbaricRecipe::getResultAmount)).apply(ins, HyperbaricRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, HyperbaricRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HyperbaricRecipe::getInput,
            ItemStack.STREAM_CODEC,
            HyperbaricRecipe::getResult,
            RecipeUtil.NUMBER_PROVIDER_STREAM_CODEC,
            HyperbaricRecipe::getResultAmount,
            HyperbaricRecipe::new);

        @Override
        public MapCodec<HyperbaricRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HyperbaricRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder extends AbstractRecipeBuilder<HyperbaricRecipe> {
        private Ingredient input;
        private ItemStack result;
        private NumberProvider resultAmount;

        @Override
        public HyperbaricRecipe buildRecipe() {
            if (resultAmount == null) {
                return new HyperbaricRecipe(input, result);
            } else {
                return new HyperbaricRecipe(input, result, resultAmount);
            }
        }

        @Override
        public void validate(ResourceLocation pId) {
            if (input == null)
                throw new IllegalArgumentException("Input cannot be null, RecipeId: " + pId);
            if (result.isEmpty())
                throw new IllegalArgumentException("Result cannot be empty, RecipeId: " + pId);
        }

        @Override
        public Item getResult() {
            return result.getItem();
        }

        @Override
        public String getType() {
            return "hyperbaric";
        }

        public HyperbaricRecipe.Builder input(final Ingredient input) {
            this.input = input;
            return this;
        }

        public HyperbaricRecipe.Builder result(final ItemStack result) {
            this.result = result;
            return this;
        }

        public HyperbaricRecipe.Builder resultAmount(final NumberProvider resultAmount) {
            this.resultAmount = resultAmount;
            return this;
        }
    }

    public Ingredient getInput() {
        return this.input;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public NumberProvider getResultAmount() {
        return this.resultAmount;
    }
}
