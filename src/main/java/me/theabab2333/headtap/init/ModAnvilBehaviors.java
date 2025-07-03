package me.theabab2333.headtap.init;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import me.theabab2333.headtap.anvil.HitBuddingBlockBehavior;
import me.theabab2333.headtap.anvil.HitHyperbaricBehavior;
import me.theabab2333.headtap.anvil.HitResinExtractorBehavior;
import me.theabab2333.headtap.anvil.HitStoneGeneratorBehavior;
import net.neoforged.neoforge.common.Tags;

public class ModAnvilBehaviors {
    public static void register() {
        IAnvilBehavior.registerBehavior(ModBlocks.STONE_GENERATOR.get(), new HitStoneGeneratorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.RESIN_EXTRACTOR.get(), new HitResinExtractorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.HYPERBARIC.get(), new HitHyperbaricBehavior());

        IAnvilBehavior.registerBehavior(state -> state.is(Tags.Blocks.BUDDING_BLOCKS), new HitBuddingBlockBehavior());
    }
}
