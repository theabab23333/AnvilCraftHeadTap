package me.theabab2333.headtap.client.init;

import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.entity.model.ThrownBambooJavelinModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModModelLayers {
    public static final ModelLayerLocation THROWN_BAMBOO_JAVELIN = new ModelLayerLocation(HeadTap.of("textures/entity/thrown_bamboo_javelin.png"), "main");

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
            THROWN_BAMBOO_JAVELIN,
            ThrownBambooJavelinModel::createBodyLayer
        );
    }

}
