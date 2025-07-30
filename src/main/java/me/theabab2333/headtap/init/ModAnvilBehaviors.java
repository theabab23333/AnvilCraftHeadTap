package me.theabab2333.headtap.init;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import me.theabab2333.headtap.anvil.HitPassiveRoyalAnvilBehavior;
import me.theabab2333.headtap.anvil.HitPassiveRoyalGrindstoneBehavior;
import me.theabab2333.headtap.anvil.HitPassiveRoyalSmithingTableBehavior;
import me.theabab2333.headtap.anvil.HitBuddingBlockBehavior;
import me.theabab2333.headtap.anvil.HitEntityEjectorBehavior;
import me.theabab2333.headtap.anvil.HitPrinterBlockBehavior;
import me.theabab2333.headtap.anvil.HitResinExtractorBehavior;
import me.theabab2333.headtap.anvil.HitStoneGeneratorBehavior;
import net.neoforged.neoforge.common.Tags;

public class ModAnvilBehaviors {
    public static void register() {
        IAnvilBehavior.registerBehavior(ModBlocks.STONE_GENERATOR.get(), new HitStoneGeneratorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.RESIN_EXTRACTOR.get(), new HitResinExtractorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_GRINDSTONE.get(), new HitPassiveRoyalGrindstoneBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_ANVIL.get(), new HitPassiveRoyalAnvilBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_TABLE.get(), new HitPassiveRoyalSmithingTableBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.ENTITY_EJECTOR.get(), new HitEntityEjectorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PRINTER.get(), new HitPrinterBlockBehavior());

        IAnvilBehavior.registerBehavior(state -> state.is(Tags.Blocks.BUDDING_BLOCKS), new HitBuddingBlockBehavior());
    }
}
