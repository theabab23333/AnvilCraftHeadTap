package me.theabab2333.headtap.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BaseShowSingleItemRenderer<B extends BlockEntity> implements BlockEntityRenderer<B> {
    // 依葫芦画瓢抄本体的BaseShowItemRenderer 一些名字都没改
    private final ItemRenderer itemRenderer;

    public BaseShowSingleItemRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    abstract ItemStack getDisplayItemStack(B blockEntity);

    abstract int getSeed(B blockEntity);

    abstract float getY();

    @Override
    @SuppressWarnings("deprecation")
    public void render(
        B blockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay) {
        Level level = blockEntity.getLevel();
        ItemStack itemStack = getDisplayItemStack(blockEntity);
        if (itemStack == null || itemStack.isEmpty()) return;
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, level, null, getSeed(blockEntity));
        poseStack.pushPose();
        float transformedGroundScaleY = bakedModel
            .getTransforms()
            .getTransform(ItemDisplayContext.GROUND)
            .scale
            .y();
        poseStack.translate(0.5F, (0.5F * transformedGroundScaleY + 0.15f) + getY(), 0.5F);
        float rotation = (Objects.requireNonNull(blockEntity.getLevel()).getGameTime() + partialTick) * 2.5f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.pushPose();
        this.itemRenderer.render(
            itemStack,
            ItemDisplayContext.GROUND,
            false,
            poseStack,
            buffer,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            bakedModel
        );
        poseStack.popPose();
        poseStack.popPose();
    }
}
