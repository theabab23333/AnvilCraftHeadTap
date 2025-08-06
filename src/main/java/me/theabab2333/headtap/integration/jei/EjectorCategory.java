package me.theabab2333.headtap.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
import dev.dubhe.anvilcraft.util.RenderHelper;
import me.theabab2333.headtap.init.ModBlockStateProperties;
import me.theabab2333.headtap.init.ModBlocks;
import me.theabab2333.headtap.init.ModRecipeTypes;
import me.theabab2333.headtap.recipe.EjectorRecipe;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EjectorCategory implements IRecipeCategory<RecipeHolder<EjectorRecipe>> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;
    private final ITickTimer timer;

    private final IDrawable arrowIn;
    private final IDrawable arrowOut;

    private static final String KEY_CATEGORY = "jei.headtap.category.ejector";
    private static final String KEY_REQUIRED_HEIGHT = KEY_CATEGORY + ".high";

    public EjectorCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(ModBlocks.ENTITY_EJECTOR.asStack());
        slot = guiHelper.getSlotDrawable();
        timer = guiHelper.createTickTimer(30, 60, true);
        title = Component.translatable("jei.headtap.category.ejector");

        arrowIn = guiHelper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 31, 16, 8);
        arrowOut = guiHelper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 40, 16, 10);
    }

    @Override
    public RecipeType<RecipeHolder<EjectorRecipe>> getRecipeType() {
        return HeadTapJeiPlugin.EJECT;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EjectorRecipe> recipeHolder, IFocusGroup focuses) {
        EjectorRecipe recipe = recipeHolder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 24)
            .addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 24)
            .addItemStack(recipe.getResult());
    }

    public static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
            HeadTapJeiPlugin.EJECT,
            JeiRecipeUtil.getRecipeHoldersFromType(ModRecipeTypes.EJECTOR.get())
        );
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENTITY_EJECTOR), HeadTapJeiPlugin.EJECT);
    }

    @Override
    public void draw(
        RecipeHolder<EjectorRecipe> recipeHolder,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY
    ) {
        EjectorRecipe recipe = recipeHolder.value();
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);

        BlockState blockState;
        ItemStack stack;
        boolean power;
        if (anvilYOffset <= -4) {
            stack = recipe.getResult();
            power = true;
        } else {
            ItemStack[] ingredientItems = recipe.getIngredient().getItems();
            stack = ingredientItems[0];
            power = false;
        }
        blockState = ModBlocks.ENTITY_EJECTOR.getDefaultState().setValue(BlockStateProperties.POWERED, power);

        RenderHelper.renderBlock(
            guiGraphics,
            blockState,
            81,
            40,
            10,
            12,
            RenderHelper.SINGLE_BLOCK);

        PoseStack poseStack = guiGraphics.pose();
        if (stack == null) return;
        RenderHelper.renderItemWithTransparency(
            stack,
            poseStack,
            73,
            (int) (10 + anvilYOffset),
            0.5f
        );

        arrowIn.draw(guiGraphics, 48, 29);
        arrowOut.draw(guiGraphics, 97, 28);

        JeiSlotUtil.drawInputSlots(guiGraphics, slot, 1);
        JeiSlotUtil.drawOutputSlots(guiGraphics, slot, 1);

        guiGraphics.drawString(Minecraft.getInstance().font,
            Component.translatable(KEY_REQUIRED_HEIGHT, recipe.getHigh()),
            0, 55, 0xFF000000, false);
    }

    @Override
    public void getTooltip(
        ITooltipBuilder tooltip,
        RecipeHolder<EjectorRecipe> recipeHolder,
        IRecipeSlotsView recipeSlotsView,
        double mouseX,
        double mouseY) {
        if (mouseX >= 72 && mouseX <= 90) {
            if (mouseY >= 34 && mouseY <= 53) {
                tooltip.add(ModBlocks.ENTITY_EJECTOR.get().getName());
            }
        }
    }
}
