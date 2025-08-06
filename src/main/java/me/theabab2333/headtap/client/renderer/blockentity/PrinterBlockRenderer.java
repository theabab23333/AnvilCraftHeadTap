package me.theabab2333.headtap.client.renderer.blockentity;

import me.theabab2333.headtap.block.entity.PrinterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PrinterBlockRenderer extends BaseShowSingleItemRenderer<PrinterBlockEntity>{
    public PrinterBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    @NotNull
    ItemStack getDisplayItemStack(@NotNull PrinterBlockEntity blockEntity) {
        return blockEntity.getDisplayItemStack();
    }

    @Override
    int getSeed(@NotNull PrinterBlockEntity blockEntity) {
        return blockEntity.getId();
    }

    @Override
    float getY() {
        return 0f;
    }
}
