package me.theabab2333.head_tap.event;

import dev.dubhe.anvilcraft.api.event.anvil.AnvilFallOnLandEvent;
import me.theabab2333.head_tap.Head_tap;
import me.theabab2333.head_tap.init.ModAnvilBehaviors;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Head_tap.MODID)
public class AnvilEventListener {
    private static boolean behaviorRegistered = false;
    @SubscribeEvent
    public static void onLand(@NotNull AnvilFallOnLandEvent event){
        if (!behaviorRegistered) {
            ModAnvilBehaviors.register();
            behaviorRegistered = true;
        }
    }
}
