package me.theabab2333.skyland_extend.item;

import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import me.theabab2333.skyland_extend.init.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AmethystAnvilHammer extends AnvilHammerItem {
    /**
     * 初始化铁砧锤
     *
     * @param properties 物品属性
     */
    public AmethystAnvilHammer(Properties properties) {
        super(properties);
    }

    @Override
    protected float getAttackDamageModifierAmount() {
        return 3;
    }
    @Override
    public Block getAnvil(){
        return ModBlocks.AMETHYST_ANVIL.get();
    }

    @Override
    protected float calculateFallDamageBonus(float fallDistance) {
        return Math.min(20, fallDistance * 2);
    }
}
