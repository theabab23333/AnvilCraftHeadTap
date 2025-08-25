package me.theabab2333.headtap.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.dubhe.anvilcraft.api.itemhandler.SlotItemHandlerWithFilter;
import dev.dubhe.anvilcraft.client.gui.screen.BaseMachineScreen;
import dev.dubhe.anvilcraft.client.gui.screen.IFilterScreen;
import dev.dubhe.anvilcraft.init.reicpe.ModRecipeTypes;
import dev.dubhe.anvilcraft.network.SlotDisableChangePacket;
import dev.dubhe.anvilcraft.network.SlotFilterChangePacket;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;
import dev.dubhe.anvilcraft.util.LevelLike;
import dev.dubhe.anvilcraft.util.RecipeUtil;
import dev.dubhe.anvilcraft.util.VertexConsumerWithPose;
import me.theabab2333.headtap.HeadTap;
import me.theabab2333.headtap.inventory.BuilderMenu;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.theabab2333.headtap.inventory.BuilderMenu.resultStack;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BuilderScreen extends BaseMachineScreen<BuilderMenu>
    implements IFilterScreen<BuilderMenu> {
    // 来自本体的BatchCrafterScreen和MultiBlockCraftingCategory......(太多了

    private static final ResourceLocation CONTAINER_LOCATION = HeadTap.of("textures/gui/builder.png");
    private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
    private final Map<RecipeHolder<MultiblockRecipe>, LevelLike> cache = new HashMap<>();

    private final BuilderMenu menu;

    public BuilderScreen(BuilderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.menu = menu;
        this.setDirectionButtonSupplier(getDirectionButtonSupplier(21, 50));
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
        this.renderBlock(guiGraphics);
    }

    // 来自本体的MultiBlockCraftingCategory
    private void renderBlock(GuiGraphics guiGraphics) {
        Level clientLevel = menu.getLevel();
        List<RecipeHolder<MultiblockRecipe>> recipeHolderList =
            clientLevel.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MULTIBLOCK_TYPE.get());
        for (RecipeHolder<MultiblockRecipe> recipe : recipeHolderList) {
            if (recipe.value().getResult().is(resultStack.getItem())) {
                LevelLike level = cache.get(recipe);
                if (level == null) {
                    level = RecipeUtil.asLevelLike(recipe.value().pattern);
                    cache.put(recipe, level);
                }

                RenderSystem.enableBlend();
                Minecraft minecraft = Minecraft.getInstance();
                DeltaTracker tracker = minecraft.getTimer();
                PoseStack pose = guiGraphics.pose();
                int sizeX = level.horizontalSize();
                int sizeY = level.verticalSize();
                float scaleX = 40 / (sizeX * Mth.SQRT_OF_TWO);
                float scaleY = 40 / (float) sizeY;
                float scale = Math.min(scaleY, scaleX);
                pose.pushPose();
                pose.translate(getCenterX(), getCenterY(), 100);
                pose.scale(-scale, -scale, -scale);

                pose.translate(-(float) sizeX / 2, -(float) sizeY / 2, 0);
                pose.mulPose(Axis.XP.rotationDegrees(-30));

                float offsetX = (float) -sizeX / 2;
                float offsetZ = (float) -sizeX / 2 + 1;
                float rotationY = (clientLevel.getGameTime() + tracker.getGameTimeDeltaPartialTick(true)) * 2f;

                pose.translate(-offsetX, 0, -offsetZ);
                pose.mulPose(Axis.YP.rotationDegrees(rotationY + 45));

                pose.translate(offsetX, 0, offsetZ);

                Iterable<BlockPos> iter;
                iter = BlockPos.betweenClosed(BlockPos.ZERO, new BlockPos(sizeX - 1, sizeY - 1, sizeX - 1));
                pose.pushPose();
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                pose.translate(0, 0, -1);
                MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();
                BlockRenderDispatcher blockRenderer = minecraft.getBlockRenderer();
                for (BlockPos pos : iter) {
                    BlockState state = level.getBlockState(pos);
                    pose.pushPose();
                    pose.translate(pos.getX(), pos.getY(), pos.getZ());
                    FluidState fluid = state.getFluidState();
                    if (!fluid.isEmpty()) {
                        RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluid);
                        VertexConsumer vertex = buffers.getBuffer(renderType);
                        blockRenderer.renderLiquid(pos, level, new VertexConsumerWithPose(vertex, pose.last(), pos), state, fluid);
                    }
                    if (state.getRenderShape() != RenderShape.INVISIBLE) {
                        BakedModel bakedModel = blockRenderer.getBlockModel(state);
                        for (RenderType type : bakedModel.getRenderTypes(state, RANDOM, ModelData.EMPTY)) {
                            VertexConsumer vertex = buffers.getBuffer(type);
                            blockRenderer.renderBatched(state, pos, level, pose, vertex, false, RANDOM, ModelData.EMPTY, type);
                        }
                    }
                    pose.popPose();
                }
                buffers.endBatch();
                pose.popPose();
                pose.popPose();
                break;
            }
        }
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

    private int getCenterX() {
        return ((this.width) / 2) - 14;
    }

    private int getCenterY() {
        return ((this.height) / 2) - 40;
    }
}
