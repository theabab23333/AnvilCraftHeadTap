package me.theabab2333.headtap.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.entity.ThrownBambooJavelinEntity;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityEntry<? extends ThrownBambooJavelinEntity> THROWN_BAMBOO_JAVELIN = HeadTap.REGISTRATE
        .<ThrownBambooJavelinEntity>entity("thrown_bamboo_javelin", ThrownBambooJavelinEntity::new, MobCategory.MISC)
        .properties(it -> it.sized(0.5F, 0.5F)
            .eyeHeight(0.13F)
            .clientTrackingRange(4)
            .updateInterval(20))
        .renderer(() -> ThrownBambooJavelinEntity.BambooJavelinRenderer::new)
        .register();
    public static void register() {
        // intentionally empty
    }
}
