package me.theabab2333.headtap.init.block;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import me.theabab2333.headtap.block.entity.ArtificialHighTemperatureDeviceBlockEntity;
import me.theabab2333.headtap.block.entity.BuilderBlockEntity;
import me.theabab2333.headtap.block.entity.CreatureExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.DistributorBlockEntity;
import me.theabab2333.headtap.block.entity.EnvironmentExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.JadeFurnaceBlockEntity;
import me.theabab2333.headtap.block.entity.JadeWorldBlockEntity;
import me.theabab2333.headtap.block.entity.LootGeneratorBlockEntity;
import me.theabab2333.headtap.block.entity.PassiveRoyalAnvilBlockEntity;
import me.theabab2333.headtap.block.entity.PassiveRoyalGrindstoneBlockEntity;
import me.theabab2333.headtap.block.entity.PassiveRoyalSmithingTableBlockEntity;
import me.theabab2333.headtap.block.entity.PrinterBlockEntity;
import me.theabab2333.headtap.block.entity.ResinExtractorBlockEntity;
import me.theabab2333.headtap.block.entity.StoneGeneratorBlockEntity;
import me.theabab2333.headtap.block.entity.SuperMassGeneratorBlockEntity;
import me.theabab2333.headtap.block.entity.VariableFluidTankBlockEntity;
import me.theabab2333.headtap.client.renderer.blockentity.PrinterBlockRenderer;

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

    public static final BlockEntityEntry<PassiveRoyalGrindstoneBlockEntity> PASSIVE_ROYAL_GRINDSTONE = REGISTRATE
        .blockEntity("passive_royal_grindstone", PassiveRoyalGrindstoneBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_GRINDSTONE)
        .register();

    public static final BlockEntityEntry<PassiveRoyalAnvilBlockEntity> PASSIVE_ROYAL_ANVIL = REGISTRATE
        .blockEntity("passive_royal_anvil", PassiveRoyalAnvilBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_ANVIL)
        .register();

    public static final BlockEntityEntry<PassiveRoyalSmithingTableBlockEntity> PASSIVE_ROYAL_TABLE = REGISTRATE
        .blockEntity("passive_royal_smithing", PassiveRoyalSmithingTableBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_TABLE)
        .register();

    public static final BlockEntityEntry<PrinterBlockEntity> PRINTER = REGISTRATE
        .blockEntity("printer", PrinterBlockEntity::new)
        .validBlock(ModBlocks.PRINTER)
        .renderer(() -> PrinterBlockRenderer::new)
        .register();

    public static final BlockEntityEntry<BuilderBlockEntity> BUILDER = REGISTRATE
        .blockEntity("builder", BuilderBlockEntity::new)
        .validBlock(ModBlocks.BUILDER)
        .register();

    public static final BlockEntityEntry<DistributorBlockEntity> DISTRIBUTER = REGISTRATE
        .blockEntity("distributor", DistributorBlockEntity::new)
        .validBlock(ModBlocks.DISTRIBUTER)
        .register();

    public static final BlockEntityEntry<ArtificialHighTemperatureDeviceBlockEntity> ARTIFICIAL_HIGH_TEMPERATURE_DEVICE = REGISTRATE
        .blockEntity("artificial_high_temperature_device", ArtificialHighTemperatureDeviceBlockEntity::new)
        .validBlock(ModBlocks.ARTIFICIAL_HIGH_TEMPERATURE_DEVICE)
        .register();

    public static final BlockEntityEntry<CreatureExtractorBlockEntity> CREATURE_EXTRACTOR = REGISTRATE
        .blockEntity("creature_extractor", CreatureExtractorBlockEntity::new)
        .validBlock(ModBlocks.CREATURE_EXTRACTOR)
        .register();

    public static final BlockEntityEntry<EnvironmentExtractorBlockEntity> ENVIRONMENT_EXTRACTOR = REGISTRATE
        .blockEntity("environment_extractor", EnvironmentExtractorBlockEntity::new)
        .validBlock(ModBlocks.ENVIRONMENT_EXTRACTOR)
        .register();

    public static final BlockEntityEntry<JadeFurnaceBlockEntity> JADE_FURNACE = REGISTRATE
        .blockEntity("jade_furnace", JadeFurnaceBlockEntity::new)
        .validBlock(ModBlocks.JADE_FURNACE)
        .register();

    public static final BlockEntityEntry<JadeWorldBlockEntity> JADE_WORLD = REGISTRATE
        .blockEntity("jade_world", JadeWorldBlockEntity::new)
        .validBlock(ModBlocks.JADE_WORLD)
        .register();

    public static final BlockEntityEntry<LootGeneratorBlockEntity> LOOT_GENERATOR = REGISTRATE
        .blockEntity("loot_generator", LootGeneratorBlockEntity::new)
        .validBlock(ModBlocks.LOOT_GENERATOR)
        .register();

    public static final BlockEntityEntry<SuperMassGeneratorBlockEntity> SUPER_MASS_GENERATOR = REGISTRATE
        .blockEntity("super_mass_generator", SuperMassGeneratorBlockEntity::new)
        .validBlock(ModBlocks.SUPER_MASS_GENERATOR)
        .register();

    public static void register() {}
}
