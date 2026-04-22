package me.theabab2333.headtap.init.block;

import me.theabab2333.headtap.HeadTap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

@MethodsReturnNonnullByDefault
public class ModBlockTags {
    public static final TagKey<Block> CAN_HIT_RANDOM = bind("can_hit_random");

    private static TagKey<Block> bind(String id) {
        return TagKey.create(Registries.BLOCK, HeadTap.of(id));
    }
}
