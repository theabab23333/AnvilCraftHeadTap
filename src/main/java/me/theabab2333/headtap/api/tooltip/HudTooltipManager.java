package me.theabab2333.headtap.api.tooltip;

import dev.dubhe.anvilcraft.api.tooltip.providers.ITooltipProvider;
import me.theabab2333.headtap.api.tooltip.impl.PrinterTooltipProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.dubhe.anvilcraft.api.tooltip.TooltipRenderHelper.renderTooltipWithItemIcon;

public class HudTooltipManager extends dev.dubhe.anvilcraft.api.tooltip.HudTooltipManager {

    // 这个也是直接抄本体的... 不会写 以后学会了就重写的qwq

    private static final int BACKGROUND_COLOR = 0xB3131313;
    private static final int BORDER_COLOR_TOP = 0xAC383838;
    private static final int BORDER_COLOR_BOTTOM = 0xE6242424;

    public static final HudTooltipManager INSTANCE = new HudTooltipManager();
    private final List<ITooltipProvider.BlockEntityTooltipProvider> blockEntityProviders = new ArrayList<>();

    static {
        INSTANCE.registerBlockEntityTooltip(new PrinterTooltipProvider());
    }

    public void registerBlockEntityTooltip(ITooltipProvider.BlockEntityTooltipProvider provider) {
        blockEntityProviders.add(provider);
    }

    @Override
    public void renderTooltip(
        GuiGraphics guiGraphics,
        BlockEntity entity,
        float partialTick,
        int screenWidth,
        int screenHeight
    ) {
        if (entity == null) return;
        final int tooltipPosX = screenWidth / 2 + 10;
        final int tooltipPosY = screenHeight / 2 + 10;
        Font font = Minecraft.getInstance().font;
        ITooltipProvider.BlockEntityTooltipProvider currentProvider = determineBlockEntityTooltipProvider(entity);
        if (currentProvider == null) return;
        List<Component> tooltip = currentProvider.tooltip(entity);
        if (tooltip == null || tooltip.isEmpty()) return;
        renderTooltipWithItemIcon(
            guiGraphics,
            font,
            currentProvider.icon(entity),
            tooltip,
            tooltipPosX,
            tooltipPosY,
            BACKGROUND_COLOR,
            BORDER_COLOR_TOP,
            BORDER_COLOR_BOTTOM);
    }

    private ITooltipProvider.BlockEntityTooltipProvider determineBlockEntityTooltipProvider(BlockEntity entity) {
        if (entity == null) return null;
        return blockEntityProviders.stream()
            .filter(it -> it.accepts(entity))
            .min(Comparator.comparingInt(ITooltipProvider::priority))
            .orElse(null);
    }
}
