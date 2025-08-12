package me.theabab2333.headtap;

import com.tterrag.registrate.Registrate;
import me.theabab2333.headtap.api.tooltip.ItemTooltipManager;
import me.theabab2333.headtap.data.HeadTapDataGen;
import me.theabab2333.headtap.init.ModBlockEntities;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModEntities;
import me.theabab2333.headtap.init.ModFluids;
import me.theabab2333.headtap.init.ModItemGroups;
import me.theabab2333.headtap.init.ModItems;
import me.theabab2333.headtap.init.ModMenuTypes;
import me.theabab2333.headtap.init.ModNetworks;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.util.ModInteractionMap;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

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
        ModMenuTypes.register();

        // datagen
        HeadTapDataGen.init();

        registerEvents(modEventBus);
    }

    private static void registerEvents(@NotNull IEventBus eventBus) {
        NeoForge.EVENT_BUS.addListener(HeadTap::addItemTooltips);

        eventBus.addListener(HeadTap::registerPayload);
        eventBus.addListener(HeadTap::loadComplete);
    }

    public static void registerPayload(@NotNull RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        ModNetworks.init(registrar);
    }

    public static @NotNull ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void loadComplete(@NotNull FMLLoadCompleteEvent event) {
        event.enqueueWork(ModInteractionMap::initInteractionMap);
    }

    public static void addItemTooltips(ItemTooltipEvent event) {
        ItemTooltipManager.addTooltip(event.getItemStack(), event.getToolTip());
    }
}