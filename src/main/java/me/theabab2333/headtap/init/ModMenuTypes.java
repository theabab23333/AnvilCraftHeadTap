package me.theabab2333.headtap.init;

import dev.anvilcraft.lib.v2.registrum.util.entry.MenuEntry;
import me.theabab2333.headtap.client.gui.screen.BuilderScreen;
import me.theabab2333.headtap.client.gui.screen.MachineOutputScreen;
import me.theabab2333.headtap.inventory.BuilderMenu;
import me.theabab2333.headtap.inventory.MachineOutputMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

import static me.theabab2333.headtap.HeadTap.REGISTRUM;

public class ModMenuTypes {

    @SuppressWarnings("DataFlowIssue")
    public static final MenuEntry<BuilderMenu> BUILDER = REGISTRUM
        .menu("builder", BuilderMenu::new, () -> BuilderScreen::new)
        .register();

    @SuppressWarnings("DataFlowIssue")
    public static final MenuEntry<MachineOutputMenu> MACHINE_OUTPUT = REGISTRUM
        .menu("machine_output", MachineOutputMenu::new, () -> MachineOutputScreen::new)
        .register();



    public static void register() {
    }

    public static void open(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    public static void open(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        player.openMenu(provider, pos);
    }
}
