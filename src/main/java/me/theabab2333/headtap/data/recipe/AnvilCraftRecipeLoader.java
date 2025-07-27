package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.recipe.anvil.BlockCompressRecipe;
import me.theabab2333.headtap.init.ModBlocks;

public class AnvilCraftRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        BlockCompressRecipe.builder()
            .input(dev.dubhe.anvilcraft.init.ModBlocks.HEAVY_IRON_BLOCK.get())
            .input(dev.dubhe.anvilcraft.init.ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK.get())
            .result(ModBlocks.DENSITY_CORE.get())
            .save(provider);
        BlockCompressRecipe.builder()
            .input(ModBlocks.DENSITY_CORE.get())
            .input(dev.dubhe.anvilcraft.init.ModBlocks.ROYAL_SMITHING_TABLE.get())
            .result(ModBlocks.PASSIVE_ROYAL_TABLE.get())
            .save(provider);
        BlockCompressRecipe.builder()
            .input(ModBlocks.DENSITY_CORE.get())
            .input(dev.dubhe.anvilcraft.init.ModBlocks.ROYAL_ANVIL.get())
            .result(ModBlocks.PASSIVE_ROYAL_ANVIL.get())
            .save(provider);
        BlockCompressRecipe.builder()
            .input(ModBlocks.DENSITY_CORE.get())
            .input(dev.dubhe.anvilcraft.init.ModBlocks.ROYAL_GRINDSTONE.get())
            .result(ModBlocks.PASSIVE_ROYAL_GRINDSTONE.get())
            .save(provider);
    }
}
