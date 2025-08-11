package me.theabab2333.headtap.mixin;

import dev.dubhe.anvilcraft.init.ModComponents;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.HasMobBlockItem;
import dev.dubhe.anvilcraft.item.ResinBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.dubhe.anvilcraft.item.ResinBlockItem.spawnMobFromItem;

@Mixin(ResinBlockItem.class)
public abstract class ResinBlockItemMixin extends HasMobBlockItem {

    public ResinBlockItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        assert player != null;
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        // 鬼知道我为什么要重新写spawnMobFromItem什么的
        // 但是不写这些又不会写(指保存的树脂块会有奇怪的标签导致不能使用
        if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity && !hasMob(stack)) {
            BaseSpawner baseSpawner = spawnerBlockEntity.getSpawner();
            Entity entity = baseSpawner.getOrCreateDisplayEntity(level, pos);
            if (entity instanceof Mob mob) {
                if (level.isClientSide()) return;
                SavedEntity savedEntity = SavedEntity.fromMob(mob);
                stack = stack.split(1);
                stack.set(ModComponents.SAVED_ENTITY, savedEntity);
                player.getInventory().placeItemBackInInventory(stack);
            }
        } else if (hasMob(stack)) {
            BlockPos mobPos = pos.relative(context.getClickedFace());
            spawnMobFromItem(level, mobPos, stack);
            RandomSource random = level.getRandom();
            ItemStack back = new ItemStack(ModItems.RESIN.asItem(), random.nextInt(1, 4));
            if (!player.getAbilities().instabuild) {
                player.getInventory().placeItemBackInInventory(back);
            }
        } else return;
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
