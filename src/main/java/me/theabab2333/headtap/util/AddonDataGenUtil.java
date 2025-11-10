package me.theabab2333.headtap.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;

public class AddonDataGenUtil {
    public static <E extends Block> void simple(DataGenContext<Block, E> context, RegistrateBlockstateProvider provider) {
        provider.simpleBlock(context.get(), AddonDangerUtil.genConfiguredModel("block/" + context.getId().getPath()).get());
    }

    private AddonDataGenUtil() {
    }
}
