package me.theabab2333.headtap.util;

import dev.anvilcraft.lib.v2.registrum.providers.DataGenContext;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumBlockstateProvider;
import net.minecraft.world.level.block.Block;

public class AddonDataGenUtil {
    public static <E extends Block> void simple(DataGenContext<Block, E> context, RegistrumBlockstateProvider provider) {
        provider.simpleBlock(context.get(), AddonDangerUtil.genConfiguredModel("block/" + context.getId().getPath()).get());
    }

    private AddonDataGenUtil() {
    }
}
