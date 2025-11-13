package me.theabab2333.headtap.client.gui.screen;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.BaseMachineScreen;
import lombok.Getter;
import me.theabab2333.headtap.inventory.MachineOutputMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

@Getter
public class OutputMachineScreen extends BaseMachineScreen<MachineOutputMenu> {
    private static final ResourceLocation CONTAINER_LOCATION = AnvilCraft.of("textures/gui/container/machine/background/auto_crafter.png");
    private final MachineOutputMenu menu;

    public OutputMachineScreen(MachineOutputMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.menu = menu;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    public void setOutputEnabled(boolean enabled) {
        menu.getBlockEntity().setEnabled(enabled);
    }

    public void getOutputEnabled() {

    }
}
