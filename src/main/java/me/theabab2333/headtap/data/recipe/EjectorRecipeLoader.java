package me.theabab2333.headtap.data.recipe;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;
import me.theabab2333.headtap.init.item.ModItems;
import me.theabab2333.headtap.recipe.EjectorRecipe;
import net.minecraft.world.item.Items;

public class EjectorRecipeLoader {
    public static void init(RegistrumRecipeProvider provider) {
        EjectorRecipe.builder()
            .requires(Items.GOLD_INGOT)
            .high(32)
            .result(ModItems.BLESSED_GOLD_INGOT)
            .save(provider);
    }
}
