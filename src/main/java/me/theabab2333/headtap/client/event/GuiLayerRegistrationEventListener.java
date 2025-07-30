package me.theabab2333.headtap.client.event;

import com.mojang.blaze3d.platform.Window;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.api.tooltip.HudTooltipManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public class GuiLayerRegistrationEventListener extends dev.dubhe.anvilcraft.client.event.GuiLayerRegistrationEventListener {

    // 抄本体的 不知道怎么写就直接抄过来了 以后学会其他方法会改的qwq

    public static void onRegister(RegisterGuiLayersEvent event) {
        event.registerAboveAll(HeadTap.of("power"), (guiGraphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.options.hideGui) return;
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(
                Minecraft.getInstance().isPaused()
            );
            Window window = Minecraft.getInstance().getWindow();
            int screenWidth = window.getGuiScaledWidth();
            int screenHeight = window.getGuiScaledHeight();
            if (minecraft.player == null || minecraft.isPaused()) return;
            if (minecraft.screen != null) return;
            ItemStack mainHandItem = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHandItem = minecraft.player.getItemInHand(InteractionHand.OFF_HAND);
            ItemStack handItem = mainHandItem.isEmpty() ? offHandItem : mainHandItem;
            if (!handItem.isEmpty()) {
                HudTooltipManager.INSTANCE.renderHandItemHudTooltip(
                    guiGraphics,
                    handItem,
                    partialTick,
                    screenWidth,
                    screenHeight
                );
            }
            HitResult hit = minecraft.hitResult;
            if (hit == null || hit.getType() != HitResult.Type.BLOCK) {
                return;
            }
            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hit).getBlockPos();
                if (minecraft.level == null) return;
                BlockEntity e = minecraft.level.getBlockEntity(blockPos);
                if (e == null) {
                    BlockState s = minecraft.level.getBlockState(blockPos);
                    if (s.is(BlockTags.AIR)) return;
                    HudTooltipManager.INSTANCE.renderTooltip(
                        guiGraphics, minecraft.level, blockPos, s, partialTick, screenWidth, screenHeight);
                    return;
                }
                HudTooltipManager.INSTANCE.renderTooltip(guiGraphics, e, partialTick, screenWidth, screenHeight);
            }
        });
    }
}
