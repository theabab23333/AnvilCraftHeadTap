package me.theabab2333.head_tap.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import me.theabab2333.head_tap.block.entity.ExcavatorBlockEntity;
import me.theabab2333.head_tap.block.entity.StoneGeneratorBlockEntity;

import static me.theabab2333.head_tap.Head_tap.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<StoneGeneratorBlockEntity> STONE_GENERATOR = REGISTRATE
            .blockEntity("stone_generator", StoneGeneratorBlockEntity::new)
            .validBlock(ModBlocks.STONE_GENERATOR)
            .register();

    public static final BlockEntityEntry<ExcavatorBlockEntity> EXCAVATOR = REGISTRATE
            .blockEntity("excavator", ExcavatorBlockEntity::new)
            .validBlock(ModBlocks.EXCAVATOR)
            .register();

    public static void register() {}
}
