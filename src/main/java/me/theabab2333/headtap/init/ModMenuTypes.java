package me.theabab2333.headtap.init;

import com.tterrag.registrate.util.entry.MenuEntry;
import me.theabab2333.headtap.client.gui.screen.BuilderScreen;
import me.theabab2333.headtap.inventory.BuilderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

import static me.theabab2333.headtap.HeadTap.REGISTRATE;

public class ModMenuTypes {

    @SuppressWarnings("DataFlowIssue")
    public static final MenuEntry<BuilderMenu> BUILDER = REGISTRATE
        .menu("builder", BuilderMenu::new, () -> BuilderScreen::new)
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
