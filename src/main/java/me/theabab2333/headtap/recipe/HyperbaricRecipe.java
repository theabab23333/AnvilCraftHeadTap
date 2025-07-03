package me.theabab2333.headtap.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.recipe.anvil.AbstractItemProcessRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractItemProcessBuilder;
import dev.dubhe.anvilcraft.util.CodecUtil;
import me.theabab2333.headtap.init.ModRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HyperbaricRecipe extends AbstractItemProcessRecipe {
    public HyperbaricRecipe(NonNullList<Ingredient> ingredients, List<ChanceItemStack> result) {
        super(ingredients, result);
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
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

    public static class Serializer implements RecipeSerializer<HyperbaricRecipe> {
        private static final MapCodec<HyperbaricRecipe> CODEC = RecordCodecBuilder.mapCodec(
            ins -> ins.group(
                CodecUtil.createIngredientListCodec("ingredients", 9, "item_crush")
                    .forGetter(HyperbaricRecipe::getIngredients),
                ChanceItemStack.CODEC.listOf().fieldOf("results").forGetter(HyperbaricRecipe::getResults))
                .apply(ins, HyperbaricRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, HyperbaricRecipe> STREAM_CODEC =
            StreamCodec.of(Serializer::encode, Serializer::decode);

        @Override
        public MapCodec<HyperbaricRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HyperbaricRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static HyperbaricRecipe decode(RegistryFriendlyByteBuf buf) {
            List<ChanceItemStack> results = new ArrayList<>();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++) {
                results.add(ChanceItemStack.STREAM_CODEC.decode(buf));
            }
            size = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            return new HyperbaricRecipe(ingredients, results);
        }

        private static void encode(RegistryFriendlyByteBuf buf, HyperbaricRecipe recipe) {
            buf.writeVarInt(recipe.results.size());
            for (ChanceItemStack stack : recipe.results) {
                ChanceItemStack.STREAM_CODEC.encode(buf, stack);
            }
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
        }
    }

    public static class Builder extends AbstractItemProcessBuilder<HyperbaricRecipe> {
        @Override
        public HyperbaricRecipe buildRecipe() {
            return new HyperbaricRecipe(ingredients, results);
        }

        @Override
        public String getType() {
            return "hyperbaric";
        }
    }
}
