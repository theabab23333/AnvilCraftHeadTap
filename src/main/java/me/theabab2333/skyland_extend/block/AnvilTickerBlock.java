package me.theabab2333.skyland_extend.block;


import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.FallingBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AnvilTickerBlock extends FallingBlock {

    public static final MapCodec<AnvilTickerBlock> CODEC = simpleCodec(AnvilTickerBlock::new);

    public AnvilTickerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}
