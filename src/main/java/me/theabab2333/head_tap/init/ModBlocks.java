package me.theabab2333.head_tap.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.init.ModBlockTags;
import me.theabab2333.head_tap.block.AmethystAnvilBlock;
import me.theabab2333.head_tap.block.StoneGeneratorBlock;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import static me.theabab2333.head_tap.Head_tap.REGISTRATE;

@SuppressWarnings("unused")
public class ModBlocks {

    static {
        REGISTRATE.defaultCreativeTab(ModItemGroups.ANVILCRAFT_HEAD_TAP.getKey());
    }

    public static final BlockEntry<? extends Block> AMETHYST_ANVIL = REGISTRATE
            .block("amethyst_anvil", AmethystAnvilBlock::new)
            .recipe((c, p) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                            .pattern("AAA")
                            .pattern(" B ")
                            .pattern("BBB")
                            .define('A', Items.AMETHYST_BLOCK)
                            .define('B', Items.AMETHYST_SHARD)
                            .unlockedBy("has_amethyst_shard", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                            .unlockedBy("has_amethyst_block", RegistrateRecipeProvider.has(Items.AMETHYST_BLOCK))
                            .save(p)
            )
            .initialProperties(() -> Blocks.ANVIL)
            .initialProperties(() -> Blocks.AMETHYST_BLOCK)
            .properties(p -> p.lightLevel(p_152632_ -> 5))
            .properties(p -> p.sound(SoundType.AMETHYST))
            .blockstate((c, p) -> {
            })
            .item()
            .build()
            .tag(
                    ModBlockTags.HAMMER_CHANGEABLE,
                    ModBlockTags.HAMMER_REMOVABLE,
                    BlockTags.ANVIL,
                    BlockTags.MINEABLE_WITH_PICKAXE,
                    BlockTags.NEEDS_STONE_TOOL)
            .register();
    public static final BlockEntry<StoneGeneratorBlock> STONE_GENERATOR = REGISTRATE
            .block("stone_generator", StoneGeneratorBlock::new)
            .recipe((c, p) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                            .pattern("ABA")
                            .pattern("C C")
                            .pattern("ADA")
                            .define('A', Items.COPPER_INGOT)
                            .define('B', Items.AMETHYST_SHARD)
                            .define('C', Blocks.GLASS)
                            .define('D', Blocks.STONECUTTER)
                            .unlockedBy("has_copper_ingot", RegistrateRecipeProvider.has(Items.COPPER_INGOT))
                            .unlockedBy("has_amethyst_shard", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                            .unlockedBy("has_glass", RegistrateRecipeProvider.has(Blocks.GLASS))
                            .unlockedBy("has_stonecutter", RegistrateRecipeProvider.has(Blocks.STONECUTTER))
                            .save(p)
                    )
            .properties(p -> p.sound(SoundType.COPPER).strength(3))
            .tag(
                    ModBlockTags.HAMMER_REMOVABLE,
                    ModBlockTags.HAMMER_CHANGEABLE,
                    BlockTags.MINEABLE_WITH_PICKAXE,
                    BlockTags.NEEDS_STONE_TOOL
            )
            .item()
            .build()
            .register();
    public static void register() {}
}