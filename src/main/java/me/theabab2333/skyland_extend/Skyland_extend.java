package me.theabab2333.skyland_extend;

import com.tterrag.registrate.Registrate;
import me.theabab2333.skyland_extend.init.ModBlocks;
import me.theabab2333.skyland_extend.init.ModItemGroups;
import me.theabab2333.skyland_extend.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Skyland_extend.MODID)
public class Skyland_extend {
    public static final String MODID = "skyland_extend";
    public static final String MODNAME = "SkylandExtend";
    private static final Logger LOGGER = LoggerFactory.getLogger(MODNAME);
    public static final Registrate REGISTRATE = Registrate.create(MODID);

    public Skyland_extend(IEventBus modEventBus) {
        ModItemGroups.register(modEventBus);
        ModBlocks.register();
        ModItems.register();
    }

    public static @NotNull ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}