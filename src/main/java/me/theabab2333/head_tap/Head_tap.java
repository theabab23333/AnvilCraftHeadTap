package me.theabab2333.head_tap;

import com.tterrag.registrate.Registrate;
import me.theabab2333.head_tap.init.ModBlockEntities;
import me.theabab2333.head_tap.init.ModBlocks;
import me.theabab2333.head_tap.init.ModItemGroups;
import me.theabab2333.head_tap.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod(Head_tap.MODID)
public class Head_tap {
    public static final String MODID = "head_tap";
    public static final Registrate REGISTRATE = Registrate.create(MODID);

    public Head_tap(IEventBus modEventBus) {
        ModItemGroups.register(modEventBus);
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
    }

    public static @NotNull ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}