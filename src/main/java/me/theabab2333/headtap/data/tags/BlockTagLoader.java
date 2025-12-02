package me.theabab2333.headtap.data.tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import me.theabab2333.headtap.init.block.ModBlockTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public class BlockTagLoader {

    private static ResourceKey<Block> findResourceKey(Block item) {
        return ResourceKey.create(Registries.BLOCK, BuiltInRegistries.BLOCK.getKey(item));
    }

    public static void init(RegistrateTagsProvider<Block> provider) {
        provider.addTag(ModBlockTags.CAN_HIT_RANDOM)
            .addTags(Tags.Blocks.BUDDING_BLOCKS)
            .addTags(BlockTags.SAPLINGS)
            .addTags(BlockTags.CROPS)
            .addTags(BlockTags.DIRT);
    }
}
