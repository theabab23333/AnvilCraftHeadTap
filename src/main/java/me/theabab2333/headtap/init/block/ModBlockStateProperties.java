package me.theabab2333.headtap.init.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final IntegerProperty HIGH;
    public static final BooleanProperty OUTPUT_ENABLE;
    public static final DirectionProperty OUTPUT_DIRECTION;

    static {
        HIGH = IntegerProperty.create("high", 0, 128);
        OUTPUT_ENABLE = BooleanProperty.create("output_enable");
        OUTPUT_DIRECTION = DirectionProperty.create("output_direction",
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN
        );
    }
}
