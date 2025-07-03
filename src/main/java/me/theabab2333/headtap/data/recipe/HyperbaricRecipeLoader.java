package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.recipe.HyperbaricRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;

public class HyperbaricRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        hyperbaric(provider, Items.IRON_INGOT, Items.STONE, 0.5f);
    }

    public static void hyperbaric(RegistrateRecipeProvider provider, ItemLike input, ItemLike result, float chance) {
        HyperbaricRecipe.builder()
            .input(Ingredient.of(input))
            .result(new ItemStack(result))
            .resultAmount(BinomialDistributionGenerator.binomial(1, chance))
            .save(
                provider,
                HeadTap.of("hyperbaric/%s/%s"
                    .formatted(
                        BuiltInRegistries.ITEM
                            .getKey(input.asItem())
                            .getPath(),
                        BuiltInRegistries.ITEM
                            .getKey(result.asItem())
                            .getPath())));
    }
}
