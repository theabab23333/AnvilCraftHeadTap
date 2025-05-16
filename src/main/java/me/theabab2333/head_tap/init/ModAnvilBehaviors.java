package me.theabab2333.head_tap.init;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import me.theabab2333.head_tap.anvil.HitStoneGeneratorBehavior;

public class ModAnvilBehaviors {
    public static void register(){
        IAnvilBehavior.registerBehavior(ModBlocks.STONE_GENERATOR.get(), new HitStoneGeneratorBehavior());
    }
}
