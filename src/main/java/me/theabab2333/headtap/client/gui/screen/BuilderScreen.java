package me.theabab2333.headtap.client.gui.screen;

import dev.dubhe.anvilcraft.api.itemhandler.SlotItemHandlerWithFilter;
import dev.dubhe.anvilcraft.client.gui.screen.BaseMachineScreen;
import dev.dubhe.anvilcraft.client.gui.screen.IFilterScreen;
import dev.dubhe.anvilcraft.network.SlotDisableChangePacket;
import dev.dubhe.anvilcraft.network.SlotFilterChangePacket;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.inventory.BuilderMenu;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BuilderScreen extends BaseMachineScreen<BuilderMenu>
    implements IFilterScreen<BuilderMenu> {
    // 来自本体的BatchCrafterScreen和MultiBlockCraftingCategory......(太多了

    private static final ResourceLocation CONTAINER_LOCATION = HeadTap.of("textures/gui/builder.png");
    private final BuilderMenu menu;

    public BuilderScreen(BuilderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.menu = menu;
        this.setDirectionButtonSupplier(getDirectionButtonSupplier(26, 50));
    }

    @Override
    protected void init() {
        super.init();
        for (Direction value : Direction.values()) {
            if (shouldSkipDirection(value)) {
                this.getDirectionButton().skip(value);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    boolean shouldSkipDirection(Direction direction) {
        return Direction.UP == direction || Direction.DOWN == direction;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONTAINER_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
        super.renderSlot(guiGraphics, slot);
        IFilterScreen.super.renderSlot(guiGraphics, slot);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        this.renderSlotTooltip(guiGraphics, x, y);
    }

    protected void renderSlotTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        if (this.hoveredSlot == null) return;
        if (!(this.hoveredSlot instanceof SlotItemHandlerWithFilter)) return;
        if (!((SlotItemHandlerWithFilter) this.hoveredSlot).isFilter()) return;
        if (!this.isFilterEnabled()) return;
        if (!this.isSlotDisabled(this.hoveredSlot.getContainerSlot())) return;
        guiGraphics.renderTooltip(this.font, Component.translatable("screen.anvilcraft.slot.disable.tooltip"), x, y);
    }

    @Override
    public @NotNull BuilderMenu getFilterMenu() {
        return this.menu;
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (type == ClickType.PICKUP) {
            if (slot instanceof SlotItemHandlerWithFilter && slot.getItem().isEmpty()) {
                ItemStack carriedItem = this.menu.getCarried();
                int realSlotId = slot.getContainerSlot();
                if (this.menu.isFilterEnabled()) {
                    PacketDistributor.sendToServer(new SlotDisableChangePacket(realSlotId, false));
                    PacketDistributor.sendToServer(new SlotFilterChangePacket(realSlotId, carriedItem.copy()));
                    menu.setSlotDisabled(realSlotId, false);
                    menu.setFilter(realSlotId, carriedItem.copy());
                    slot.set(carriedItem);
                } else {
                    if (carriedItem.isEmpty()) {
                        PacketDistributor.sendToServer(new SlotDisableChangePacket(realSlotId, !this.menu.isSlotDisabled(realSlotId)));
                    } else {
                        PacketDistributor.sendToServer(new SlotDisableChangePacket(realSlotId, false));
                    }
                }
            }
        }
        super.slotClicked(slot, slotId, mouseButton, type);
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
