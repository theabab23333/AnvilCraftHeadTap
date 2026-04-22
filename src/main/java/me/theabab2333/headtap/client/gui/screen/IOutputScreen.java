package me.theabab2333.headtap.client.gui.screen;

import me.theabab2333.headtap.client.gui.component.EnableOutputButton;
import me.theabab2333.headtap.inventory.IOutputMenu;
import me.theabab2333.headtap.network.MachineEnableOutputPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.BiFunction;

public interface IOutputScreen<T extends AbstractContainerMenu & IOutputMenu> {
    T getOutputMenu();

    default BiFunction<Integer, Integer, EnableOutputButton> getEnableOutputButtonSupplier(int x, int y) {
        return (i, j) -> new EnableOutputButton(
            i + x,
            j + y,
            button -> {
                if (button instanceof EnableOutputButton enableOutputButton) {
                    MachineEnableOutputPacket packet = new MachineEnableOutputPacket(enableOutputButton.next());
                    PacketDistributor.sendToServer(packet);
                }
            },
            this::isOutputEnabled);
    }

    default boolean isOutputEnabled() {
        return this.getOutputMenu().isOutputEnable();
    }

    default void setOutputEnable(boolean enable) {
        this.getOutputMenu().setOutputEnable(enable);
    }

    default void flush() {}

    default int getOffsetY() {
        return 0;
    }

    default int getOffsetX() {
        return 0;
    }
}
