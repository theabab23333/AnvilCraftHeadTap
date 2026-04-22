package me.theabab2333.headtap.init;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import me.theabab2333.headtap.anvil.hit.HitCanRandomBlockBehavior;
import me.theabab2333.headtap.anvil.hit.HitEntityEjectorBehavior;
import me.theabab2333.headtap.anvil.hit.HitPassiveRoyalAnvilBehavior;
import me.theabab2333.headtap.anvil.hit.HitPassiveRoyalGrindstoneBehavior;
import me.theabab2333.headtap.anvil.hit.HitPassiveRoyalSmithingTableBehavior;
import me.theabab2333.headtap.anvil.hit.HitPrinterBlockBehavior;
import me.theabab2333.headtap.anvil.hit.HitResinExtractorBehavior;
import me.theabab2333.headtap.anvil.hit.HitStoneGeneratorBehavior;
import me.theabab2333.headtap.init.block.ModBlockTags;
import me.theabab2333.headtap.init.block.ModBlocks;

public class ModAnvilBehaviors {
    public static void register() {
        IAnvilBehavior.registerBehavior(ModBlocks.STONE_GENERATOR.get(), new HitStoneGeneratorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.RESIN_EXTRACTOR.get(), new HitResinExtractorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_GRINDSTONE.get(), new HitPassiveRoyalGrindstoneBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_ANVIL.get(), new HitPassiveRoyalAnvilBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PASSIVE_ROYAL_TABLE.get(), new HitPassiveRoyalSmithingTableBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.ENTITY_EJECTOR.get(), new HitEntityEjectorBehavior());
        IAnvilBehavior.registerBehavior(ModBlocks.PRINTER.get(), new HitPrinterBlockBehavior());

        IAnvilBehavior.registerBehavior(state -> state.is(ModBlockTags.CAN_HIT_RANDOM), new HitCanRandomBlockBehavior());
    }
}
