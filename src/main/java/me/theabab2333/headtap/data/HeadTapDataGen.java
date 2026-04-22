package me.theabab2333.headtap.data;

import dev.anvilcraft.lib.v2.registrum.providers.ProviderType;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.data.lang.LangHandler;
import me.theabab2333.headtap.data.recipe.RecipeHandler;
import me.theabab2333.headtap.data.tags.TagsHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static me.theabab2333.headtap.HeadTap.REGISTRUM;

@EventBusSubscriber(modid = HeadTap.MOD_ID)
public class HeadTapDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
    }

    public static void init() {
        REGISTRUM.addDataGenerator(ProviderType.ITEM_TAGS, TagsHandler::initItem);
        REGISTRUM.addDataGenerator(ProviderType.BLOCK_TAGS, TagsHandler::initBlock);

        REGISTRUM.addDataGenerator(ProviderType.RECIPE, RecipeHandler::init);
        REGISTRUM.addDataGenerator(ProviderType.LANG, LangHandler::init);
    }
}
