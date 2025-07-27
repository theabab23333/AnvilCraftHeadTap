package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import me.theabab2333.headtap.init.ModItems;
import me.theabab2333.headtap.recipe.EjectorRecipe;
import net.minecraft.world.item.Items;

public class EjectorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        EjectorRecipe.builder()
            .requires(Items.GOLD_INGOT)
            .high(32)
            .result(ModItems.BLESSED_GOLD_INGOT)
            .save(provider);
    }
}
