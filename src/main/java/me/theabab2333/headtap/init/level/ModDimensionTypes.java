package me.theabab2333.headtap.init.level;

import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import me.theabab2333.headtap.HeadTap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class ModDimensionTypes { // coordisna
    public static final ResourceKey<DimensionType> JADE = key("jade");
    public static final ResourceKey<Level> JADE_LEVEL = ResourceKey.create(Registries.DIMENSION, HeadTap.of("jade"));

    public static void bootstrap(BootstrapContext<DimensionType> context) {
        context.register(
            JADE,
            new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                false,
                1,
                false,
                true,
                -64,
                384,
                384,
                ModBlockTags.HEATED_BLOCKS,
                JADE_LEVEL.location(),
                15,
                new DimensionType.MonsterSettings(
                    false,
                    false,
                    ConstantInt.ZERO,
                    0
                )
            )
        );
    }

    private static ResourceKey<DimensionType> key(String id) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, HeadTap.of(id));
    }
}
