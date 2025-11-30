package me.theabab2333.headtap.client.gui.screen;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.BaseMachineScreen;
import lombok.Getter;
import me.theabab2333.headtap.client.gui.component.EnableOutputButton;
import me.theabab2333.headtap.inventory.MachineOutputMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

@Getter
public class MachineOutputScreen extends BaseMachineScreen<MachineOutputMenu> implements IOutputScreen<MachineOutputMenu> {
    private static final ResourceLocation CONTAINER_LOCATION = AnvilCraft.of("textures/gui/container/machine/background/auto_crafter.png");
    BiFunction<Integer, Integer, EnableOutputButton> enableOutputButtonSupplier = this.getEnableOutputButtonSupplier(116, 18);
    private final MachineOutputMenu menu;
    private EnableOutputButton enableOutputButton = null;

    public MachineOutputScreen(
        MachineOutputMenu menu,
        Inventory inventory,
        Component title
    ) {
        super(menu, inventory, title);
        this.menu = menu;
    }

    @Override
    protected void renderBg(
        @NotNull GuiGraphics guiGraphics,
        float v,
        int mouseX,
        int mouseY
    ) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(
            CONTAINER_LOCATION,
            i,
            j,
            0,
            0,
            this.imageWidth,
            this.imageHeight
        );
    }

    @Override
    protected void init() {
        super.init();
        this.enableOutputButton = enableOutputButtonSupplier.apply(this.leftPos, this.topPos);
        this.addRenderableWidget(this.enableOutputButton);
    }

    @Override
    public void render(
        @NotNull GuiGraphics guiGraphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public MachineOutputMenu getOutputMenu() {
        return this.menu;
    }

    @Override
    public void flush() {
        this.enableOutputButton.flush();
    }

    @Override
    public int getOffsetX() {
        return (this.width - this.imageWidth) / 2;
    }

    @Override
    public int getOffsetY() {
        return (this.height - this.imageHeight) / 2;
    }
}
