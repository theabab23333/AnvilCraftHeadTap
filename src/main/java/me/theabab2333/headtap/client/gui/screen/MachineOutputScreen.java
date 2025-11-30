package me.theabab2333.headtap.client.gui.screen;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.BaseMachineScreen;
import lombok.Getter;
import me.theabab2333.headtap.client.gui.component.EnableOutputButton;
import me.theabab2333.headtap.inventory.MachineOutputMenu;
import me.theabab2333.headtap.network.MachineEnableOutputPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Getter
public class MachineOutputScreen extends BaseMachineScreen<MachineOutputMenu> {
    private static final ResourceLocation CONTAINER_LOCATION = AnvilCraft.of("textures/gui/container/machine/background/auto_crafter.png");
    private final MachineOutputMenu menu;
    protected EnableOutputButton enableOutputButton;

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
        guiGraphics.blit(CONTAINER_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void init() {
        super.init();
        this.enableOutputButton = new EnableOutputButton(
            leftPos + 75,
            topPos + 54,
            b -> {
                if (!(b instanceof EnableOutputButton button)) return;
                PacketDistributor.sendToServer(new MachineEnableOutputPacket(!button.next()));
            },
            () -> this.menu.getBlockEntity().isEnabled()
        );
        this.addRenderableWidget(this.enableOutputButton);

    }

    public void setOutputEnabled(boolean enabled) {
        menu.getBlockEntity().setEnabled(enabled);
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

    public void getOutputEnabled() {

    }
}
