package me.theabab2333.headtap.mixin;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.util.BreakBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BaseLaserBlockEntity.class)
public abstract class BaseLaserBlockEntityMixin extends BlockEntity {
    @Shadow public abstract BlockPos getIrradiateBlockPos();

    @Shadow public abstract int getLaserLevel();

    @Shadow protected int tickCount;

    @Shadow @Final public static int[] COOLDOWNS;

    @Shadow public abstract void deliverItem(List<ItemStack> drops, Direction direction, BlockPos sourceBlockPos);

    @Shadow public abstract Direction getFacing();

    public BaseLaserBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "canPassThrough", at = @At("HEAD"), cancellable = true)
    private void canPassThrough(Direction direction, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (level == null) cir.setReturnValue(false);
        BlockState blockState = level.getBlockState(blockPos);
        if (
            blockState.is(ModBlockTags.LASER_CAN_PASS_THROUGH) ||
                blockState.is(Tags.Blocks.GLASS_BLOCKS) ||
                blockState.is(Tags.Blocks.GLASS_PANES) ||
                blockState.is(BlockTags.REPLACEABLE) ||
                blockState.is(Tags.Blocks.BUDS)
        ) cir.setReturnValue(true);
        if (blockState.is(Tags.Blocks.CLUSTERS)) cir.setReturnValue(false);
        if (!AnvilCraft.CONFIG.isLaserDoImpactChecking) cir.setReturnValue(false);
        AABB laseBoundingBox = switch (direction.getAxis()) {
            case X -> Block.box(0, 7, 7, 16, 9, 9).bounds();
            case Y -> Block.box(7, 0, 7, 9, 16, 9).bounds();
            case Z -> Block.box(7, 7, 0, 9, 9, 16).bounds();
        };
        cir.setReturnValue(blockState.getCollisionShape(level, blockPos).toAabbs().stream().noneMatch(laseBoundingBox::intersects));
    }

    @Inject(method = "emitLaser", at = @At("TAIL"))
    public void emitLaser(Direction direction, CallbackInfo ci) {
        if (level == null) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        BlockState irradiateBlock = level.getBlockState(getIrradiateBlockPos());
        int cooldown = COOLDOWNS[Math.clamp(getLaserLevel() / 4, 0, 4)];
        if (tickCount <= cooldown) {
            tickCount = 0;
            if (irradiateBlock.is(Tags.Blocks.CLUSTERS)) {
                List<ItemStack> drops = BreakBlockUtil.drop(serverLevel, getIrradiateBlockPos());
                System.out.println(drops);
                deliverItem(drops, direction, getIrradiateBlockPos());
            }
        }
    }

    @Inject(method = "deliverItem", at = @At("HEAD"), cancellable = true)
    public void deliverItem(List<ItemStack> drops, Direction direction, BlockPos sourceBlockPos, CallbackInfo ci) {
        if (level == null) return;
        Vec3 blockPos = getBlockPos().relative(direction.getOpposite()).getCenter();
        BlockPos downStreamPos = getBlockPos().relative(getFacing().getOpposite());
        if (getLevel() == null) return;
        IItemHandler cap = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, downStreamPos, getFacing());
        BlockState sourceBlock = level.getBlockState(sourceBlockPos);
        drops.forEach(itemStack -> {
            if (cap != null) {
                ItemStack outItemStack = ItemHandlerHelper.insertItem(cap, itemStack, true);
                if (outItemStack.isEmpty()) {
                    ItemHandlerHelper.insertItem(cap, itemStack, false);
                } else {
                    level.addFreshEntity(new ItemEntity(level, blockPos.x, blockPos.y, blockPos.z, outItemStack));
                }
            } else if (level.getBlockEntity(downStreamPos) instanceof BaseLaserBlockEntity downStreamBlockEntity && downStreamBlockEntity.getFacing() == direction) {
                downStreamBlockEntity.deliverItem(drops, direction, sourceBlockPos);
            } else level.addFreshEntity(new ItemEntity(level, blockPos.x, blockPos.y, blockPos.z, itemStack));
        });
        if (level.getBlockEntity(downStreamPos) instanceof BaseLaserBlockEntity) return;
        if (sourceBlock.is(Blocks.ANCIENT_DEBRIS)) {
            level.setBlockAndUpdate(sourceBlockPos, Blocks.NETHERRACK.defaultBlockState());
        } else if (sourceBlock.is(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)) {
            level.setBlockAndUpdate(sourceBlockPos, Blocks.DEEPSLATE.defaultBlockState());
        } else if (sourceBlock.is(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)) {
            level.setBlockAndUpdate(sourceBlockPos, Blocks.NETHERRACK.defaultBlockState());
        } else if (sourceBlock.is(Tags.Blocks.CLUSTERS)) {
            level.setBlockAndUpdate(sourceBlockPos, Blocks.AIR.defaultBlockState());
        } else {
            level.setBlockAndUpdate(sourceBlockPos, Blocks.STONE.defaultBlockState());
        }
        ci.cancel();
    }
}
