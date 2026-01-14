package me.theabab2333.headtap.init.level;

import dev.dubhe.anvilcraft.AnvilCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModRegistryProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.DIMENSION_TYPE, ModDimensionTypes::bootstrap);

    public ModRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(AnvilCraft.MOD_ID));
    }
}
