package me.theabab2333.headtap.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import me.theabab2333.headtap.block.entity.AutoAnvilBlockEntity;
import me.theabab2333.headtap.block.entity.AutoGrindstoneBlockEntity;
import me.theabab2333.headtap.block.entity.AutoSmithingTableBlockEntity;
import me.theabab2333.headtap.block.entity.PrinterBlockEntity;
import me.theabab2333.headtap.block.entity.ResinExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;
import me.theabab2333.headtap.block.entity.VariableFluidTankBlockEntity;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<StoneGeneratorBlockEntity> STONE_GENERATOR = REGISTRATE
        .blockEntity("stone_generator", StoneGeneratorBlockEntity::new)
        .validBlock(ModBlocks.STONE_GENERATOR)
        .register();

    public static final BlockEntityEntry<ResinExtractorBlockEntity> RESIN_EXTRACTOR = REGISTRATE
        .blockEntity("resin_extractor", ResinExtractorBlockEntity::new)
        .validBlock(ModBlocks.RESIN_EXTRACTOR)
        .register();

    public static final BlockEntityEntry<VariableFluidTankBlockEntity> VARIABLE_FLUID_TANK = REGISTRATE
        .blockEntity("variable_fluid_tank", VariableFluidTankBlockEntity::createBlockEntity)
        .validBlocks(ModBlocks.VARIABLE_FLUID_TANK)
        .register();

    public static final BlockEntityEntry<AutoGrindstoneBlockEntity> PASSIVE_ROYAL_GRINDSTONE = REGISTRATE
        .blockEntity("passive_royal_grindstone", AutoGrindstoneBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_GRINDSTONE)
        .register();

    public static final BlockEntityEntry<AutoAnvilBlockEntity> PASSIVE_ROYAL_ANVIL = REGISTRATE
        .blockEntity("passive_royal_anvil", AutoAnvilBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_ANVIL)
        .register();

    public static final BlockEntityEntry<AutoSmithingTableBlockEntity> PASSIVE_ROYAL_TABLE = REGISTRATE
        .blockEntity("passive_royal_smithing", AutoSmithingTableBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_TABLE)
        .register();

    public static final BlockEntityEntry<PrinterBlockEntity> PRINTER = REGISTRATE
        .blockEntity("printer", PrinterBlockEntity::new)
        .validBlock(ModBlocks.PRINTER)
        .register();

    public static void register() {}
}
