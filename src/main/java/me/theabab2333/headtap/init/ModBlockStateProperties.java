package me.theabab2333.headtap.init;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final IntegerProperty HIGH;

    static {
        HIGH = IntegerProperty.create("high", 0, 128);
    }
}
