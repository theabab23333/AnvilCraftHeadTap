package me.theabab2333.head_tap.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.init.ModItemTags;
import me.theabab2333.head_tap.item.AmethystAnvilHammer;
import me.theabab2333.head_tap.item.GolemCraftbow;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import static me.theabab2333.head_tap.Head_tap.REGISTRATE;

@SuppressWarnings("unused")
public class ModItems {

    static {
        REGISTRATE.defaultCreativeTab(ModItemGroups.ANVILCRAFT_HEAD_TAP.getKey());
    }

    public static final ItemEntry<AmethystAnvilHammer> AMETHYST_HAMMER = REGISTRATE
            .item("amethyst_hammer", AmethystAnvilHammer::new)
            .tag(ItemTags.MACE_ENCHANTABLE, ModItemTags.bind("tools/anvil_hammer"))
            .properties(properties -> properties.durability(30))
            .model((ctx, provider) -> {
            })
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
                            RecipeCategory.TOOLS, ctx.get())
                    .pattern("A")
                    .pattern("B")
                    .pattern("C")
                    .define('A', ModBlocks.AMETHYST_ANVIL)
                    .define('B', Items.IRON_INGOT)
                    .define('C', Items.AMETHYST_BLOCK)
                    .unlockedBy("has_amethyst_anvil", RegistrateRecipeProvider.has(ModBlocks.AMETHYST_ANVIL))
                    .unlockedBy("has_iron_ingot", RegistrateRecipeProvider.has(Items.IRON_INGOT))
                    .unlockedBy("has_amethyst_block", RegistrateRecipeProvider.has(Items.AMETHYST_BLOCK))
                    .save(provider))
            .register();
    public static final ItemEntry<GolemCraftbow> GOLEM_CRAFTBOW = REGISTRATE
        .item("golem_craftbow", GolemCraftbow::new)
        .properties(properties -> properties.durability(529))
        .model((ctx, provider) -> {
        })
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
                RecipeCategory.TOOLS, ctx.get())
            .pattern("APA")
            .pattern("ADA")
            .pattern("XC ")
            .define('A', dev.dubhe.anvilcraft.init.ModBlocks.BLOCK_PLACER)
            .define('P', Items.CARVED_PUMPKIN)
            .define('D', Items.DISPENSER)
            .define('C', Items.CROSSBOW)
            .define('X', Items.SHEARS)
            .unlockedBy("has_block_placer", RegistrateRecipeProvider.has(dev.dubhe.anvilcraft.init.ModBlocks.BLOCK_PLACER))
            .unlockedBy("has_carved_pumpkin", RegistrateRecipeProvider.has(Items.CARVED_PUMPKIN))
            .unlockedBy("has_crossbow", RegistrateRecipeProvider.has(Items.CROSSBOW))
            .save(provider))
        .register();


    public static void register() {}
}
