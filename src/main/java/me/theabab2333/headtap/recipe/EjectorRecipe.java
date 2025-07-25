package me.theabab2333.headtap.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractRecipeBuilder;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.init.ModRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EjectorRecipe implements Recipe<SingleRecipeInput> {
    public final Ingredient ingredient;
    public final ItemStack result;
    public final int high;

    public EjectorRecipe(Ingredient input, ItemStack result, int high) {
        this.ingredient = input;
        this.result = result;
        this.high = high;
    }

    @Contract(" -> new")
    public static EjectorRecipe.Builder builder() {
        return new EjectorRecipe.Builder();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.EJECTOR.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.EJECTOR_SERIALIZER.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.getItem(0));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    public static class Serializer implements RecipeSerializer<EjectorRecipe> {

        private static final MapCodec<EjectorRecipe> CODEC = RecordCodecBuilder.mapCodec(
            ins -> ins.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(EjectorRecipe::getIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(EjectorRecipe::getResult),
                Codec.INT.fieldOf("high").forGetter(EjectorRecipe::getHigh))
                .apply(ins, EjectorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, EjectorRecipe> STREAM_CODEC =
            StreamCodec.of(EjectorRecipe.Serializer::encode, EjectorRecipe.Serializer::decode);

        @Override
        public MapCodec<EjectorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EjectorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void encode(RegistryFriendlyByteBuf buf, EjectorRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredient);
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
            buf.writeVarInt(recipe.high);
        }

        private static EjectorRecipe decode(RegistryFriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
            int high = buf.readVarInt();
            return new EjectorRecipe(ingredient, result, high);
        }
    }

    public static class Builder extends AbstractRecipeBuilder<EjectorRecipe> {

        private Ingredient ingredient = null;
        private ItemStack result = null;
        private int high = 0;

        @Override
        public EjectorRecipe buildRecipe() {
            return new EjectorRecipe(ingredient, result, high);
        }

        @Override
        public void validate(ResourceLocation pId) {
            if (ingredient == null) throw new IllegalArgumentException("Recipe has no ingredient, RecipeId: " + pId);
            if (result == null) throw new IllegalArgumentException("Recipe has no result, RecipeId: " + pId);
            if (high <= 0 || high > 128) throw new IllegalArgumentException("The high release of ejector recipe must be greater than 0 or less than 128, RecipeId: " + pId);
        }

        @Override
        public String getType() {
            return "ejector";
        }

        @Override
        public Item getResult() {
            return result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput) {
            save(recipeOutput, HeadTap.of(BuiltInRegistries.ITEM.getKey(getResult()).getPath()).withPrefix(getType() + "/"));
        }

        public EjectorRecipe.Builder ingredient(final Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public EjectorRecipe.Builder requires(ItemLike pItem) {
            ingredient = Ingredient.of(pItem);
            return this;
        }

        public EjectorRecipe.Builder requires(TagKey<Item> tag) {
            ingredient = Ingredient.of(tag);
            return this;
        }

        public EjectorRecipe.Builder result(ItemLike pItem) {
            result = pItem.asItem().getDefaultInstance();
            return this;
        }

        public EjectorRecipe.Builder high(int high) {
            this.high = high;
            return this;
        }
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getHigh() {
        return this.high;
    }
}
