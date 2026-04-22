package me.theabab2333.headtap.data.recipe;

import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;

public class RecipeHandler {
    public static void init(RegistrumRecipeProvider provider) {
        StoneGeneratorRecipeLoader.init(provider);
        GolemCraftRecipeLoader.init(provider);
        EjectorRecipeLoader.init(provider);

        AnvilCraftRecipeLoader.init(provider);
    }
}
