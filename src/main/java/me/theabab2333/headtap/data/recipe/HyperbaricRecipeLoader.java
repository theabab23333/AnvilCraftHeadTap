package me.theabab2333.headtap.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.init.ModItems;
import me.theabab2333.headtap.recipe.HyperbaricRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

public class HyperbaricRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        HyperbaricRecipe.builder()
            .requires(Tags.Items.CROPS_WHEAT)
            .result(new ItemStack(ModItems.FLOUR.get()))
            .save(provider);
    }
}
