package me.theabab2333.headtap;

import com.tterrag.registrate.Registrate;
import me.theabab2333.headtap.data.HeadTapDataGen;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModEntities;
import me.theabab2333.headtap.init.ModFluids;
import me.theabab2333.headtap.init.ModItemGroups;
import me.theabab2333.headtap.init.ModItems;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.util.ModInteractionMap;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(HeadTap.MOD_ID)
public class HeadTap {
    public static final String MOD_ID = "headtap";
    public static IEventBus MOD_BUS = null;
    public static final Registrate REGISTRATE = Registrate.create(MOD_ID);

    public HeadTap(IEventBus modEventBus) {
        MOD_BUS = modEventBus;
        ModItemGroups.register(modEventBus);
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModRecipeTypes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModEntities.register();

        // datagen
        HeadTapDataGen.init();

        registerEvents(modEventBus);
    }

    private static void registerEvents(@NotNull IEventBus eventBus) {
        eventBus.addListener(HeadTap::loadComplete);
    }

    public static @NotNull ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void loadComplete(@NotNull FMLLoadCompleteEvent event) {
        event.enqueueWork(ModInteractionMap::initInteractionMap);
    }
}