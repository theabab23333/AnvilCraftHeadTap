package me.theabab2333.headtap.init.block;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntityEntry;
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

import static me.theabab2333.headtap.HeadTap.REGISTRUM;

public class ModBlockEntities {

    public static final BlockEntityEntry<StoneGeneratorBlockEntity> STONE_GENERATOR = REGISTRUM
        .blockEntity("stone_generator", StoneGeneratorBlockEntity::new)
        .validBlock(ModBlocks.STONE_GENERATOR)
        .register();

    public static final BlockEntityEntry<ResinExtractorBlockEntity> RESIN_EXTRACTOR = REGISTRUM
        .blockEntity("resin_extractor", ResinExtractorBlockEntity::new)
        .validBlock(ModBlocks.RESIN_EXTRACTOR)
        .register();

    public static final BlockEntityEntry<VariableFluidTankBlockEntity> VARIABLE_FLUID_TANK = REGISTRUM
        .blockEntity("variable_fluid_tank", VariableFluidTankBlockEntity::createBlockEntity)
        .validBlocks(ModBlocks.VARIABLE_FLUID_TANK)
        .register();

    public static final BlockEntityEntry<PassiveRoyalGrindstoneBlockEntity> PASSIVE_ROYAL_GRINDSTONE = REGISTRUM
        .blockEntity("passive_royal_grindstone", PassiveRoyalGrindstoneBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_GRINDSTONE)
        .register();

    public static final BlockEntityEntry<PassiveRoyalAnvilBlockEntity> PASSIVE_ROYAL_ANVIL = REGISTRUM
        .blockEntity("passive_royal_anvil", PassiveRoyalAnvilBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_ANVIL)
        .register();

    public static final BlockEntityEntry<PassiveRoyalSmithingTableBlockEntity> PASSIVE_ROYAL_TABLE = REGISTRUM
        .blockEntity("passive_royal_smithing", PassiveRoyalSmithingTableBlockEntity::new)
        .validBlock(ModBlocks.PASSIVE_ROYAL_TABLE)
        .register();

    public static final BlockEntityEntry<PrinterBlockEntity> PRINTER = REGISTRUM
        .blockEntity("printer", PrinterBlockEntity::new)
        .validBlock(ModBlocks.PRINTER)
        .renderer(() -> PrinterBlockRenderer::new)
        .register();

    public static final BlockEntityEntry<BuilderBlockEntity> BUILDER = REGISTRUM
        .blockEntity("builder", BuilderBlockEntity::new)
        .validBlock(ModBlocks.BUILDER)
        .register();

    public static final BlockEntityEntry<DistributorBlockEntity> DISTRIBUTER = REGISTRUM
        .blockEntity("distributor", DistributorBlockEntity::new)
        .validBlock(ModBlocks.DISTRIBUTER)
        .register();

    public static final BlockEntityEntry<ArtificialHighTemperatureDeviceBlockEntity> ARTIFICIAL_HIGH_TEMPERATURE_DEVICE = REGISTRUM
        .blockEntity("artificial_high_temperature_device", ArtificialHighTemperatureDeviceBlockEntity::new)
        .validBlock(ModBlocks.ARTIFICIAL_HIGH_TEMPERATURE_DEVICE)
        .register();

    public static final BlockEntityEntry<CreatureExtractorBlockEntity> CREATURE_EXTRACTOR = REGISTRUM
        .blockEntity("creature_extractor", CreatureExtractorBlockEntity::new)
        .validBlock(ModBlocks.CREATURE_EXTRACTOR)
        .register();

    public static final BlockEntityEntry<EnvironmentExtractorBlockEntity> ENVIRONMENT_EXTRACTOR = REGISTRUM
        .blockEntity("environment_extractor", EnvironmentExtractorBlockEntity::new)
        .validBlock(ModBlocks.ENVIRONMENT_EXTRACTOR)
        .register();

    public static final BlockEntityEntry<JadeFurnaceBlockEntity> JADE_FURNACE = REGISTRUM
        .blockEntity("jade_furnace", JadeFurnaceBlockEntity::new)
        .validBlock(ModBlocks.JADE_FURNACE)
        .register();

    public static final BlockEntityEntry<JadeWorldBlockEntity> JADE_WORLD = REGISTRUM
        .blockEntity("jade_world", JadeWorldBlockEntity::new)
        .validBlock(ModBlocks.JADE_WORLD)
        .register();

    public static final BlockEntityEntry<LootGeneratorBlockEntity> LOOT_GENERATOR = REGISTRUM
        .blockEntity("loot_generator", LootGeneratorBlockEntity::new)
        .validBlock(ModBlocks.LOOT_GENERATOR)
        .register();

    public static final BlockEntityEntry<SuperMassGeneratorBlockEntity> SUPER_MASS_GENERATOR = REGISTRUM
        .blockEntity("super_mass_generator", SuperMassGeneratorBlockEntity::new)
        .validBlock(ModBlocks.SUPER_MASS_GENERATOR)
        .register();

    public static void register() {
    }
}
