package me.theabab2333.headtap.init;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import me.theabab2333.headtap.anvil.HitAutoAnvilBehavior;
import me.theabab2333.headtap.anvil.HitAutoGrindstoneBehavior;
import me.theabab2333.headtap.anvil.HitAutoSmithingTableBehavior;
import me.theabab2333.headtap.anvil.HitBuddingBlockBehavior;
import me.theabab2333.headtap.anvil.HitEntityEjectorBehavior;
import me.theabab2333.headtap.anvil.HitResinExtractorBehavior;
import me.theabab2333.headtap.anvil.HitStoneGeneratorBehavior;
import net.neoforged.neoforge.common.Tags;

public class ModAnvilBehaviors {
    public static void register() {
        IAnvilBehavior.registerBehavior(ModBlocks.STONE_GENERATOR.get(), new HitStoneGeneratorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.RESIN_EXTRACTOR.get(), new HitResinExtractorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_GRINDSTONE.get(), new HitAutoGrindstoneBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_ANVIL.get(), new HitAutoAnvilBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_TABLE.get(), new HitAutoSmithingTableBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.ENTITY_EJECTOR.get(), new HitEntityEjectorBehavior());

        IAnvilBehavior.registerBehavior(state -> state.is(Tags.Blocks.BUDDING_BLOCKS), new HitBuddingBlockBehavior());
    }
}
