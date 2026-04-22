package me.theabab2333.headtap.util;

import me.theabab2333.headtap.HeadTap;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddonDangerUtil {
    @NotNull
    public static Supplier<ConfiguredModel[]> genConfiguredModel(String path) {
        return () -> new ConfiguredModel[] {new ConfiguredModel(new ModelFile.UncheckedModelFile(HeadTap.of(path)))};
    }

    @NotNull
    public static Supplier<ModelFile> genModModelFile(String path) {
        return () -> new ModelFile.UncheckedModelFile(HeadTap.of(path));
    }

    @NotNull
    public static Supplier<ModelFile.UncheckedModelFile> genUncheckedModelFile(String path) {
        return () -> new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace(path));
    }

    @NotNull
    public static Supplier<ModelFile.UncheckedModelFile> genUncheckedModelFile(String namespace, String path) {
        return () -> new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    private AddonDangerUtil() {
    }
}
