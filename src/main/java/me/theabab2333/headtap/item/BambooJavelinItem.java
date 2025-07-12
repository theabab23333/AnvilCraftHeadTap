package me.theabab2333.headtap.item;

import me.theabab2333.headtap.entity.ThrownBambooJavelinEntity;
import me.theabab2333.headtap.init.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

public class BambooJavelinItem extends Item implements ProjectileItem {

    public BambooJavelinItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        double durabilityRatio = 1.0 - ((double) damage / maxDamage);
        durabilityRatio = Math.min(1.0, durabilityRatio);
        double baseDamage = 6.0;
        double scaledDamage = baseDamage * durabilityRatio;
        scaledDamage = Math.max(scaledDamage, 0.1);

        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                    BASE_ATTACK_DAMAGE_ID,
                    scaledDamage,
                    AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND)
            .add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                    BASE_ATTACK_SPEED_ID,
                    -2.9000000953674316,
                    AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND)
            .build();
    }

    @Override
    public @NotNull Projectile asProjectile(
        @NotNull Level level,
        @NotNull Position pos,
        @NotNull ItemStack itemStack,
        @NotNull Direction direction) {
        ThrownBambooJavelinEntity thrownBambooJavelin =
            new ThrownBambooJavelinEntity(
                ModEntities.THROWN_BAMBOO_JAVELIN.get(),
                pos.x(),
                pos.y(),
                pos.z(),
                level,
                itemStack.copyWithCount(1),
                null);
        thrownBambooJavelin.pickup = AbstractArrow.Pickup.ALLOWED;
        return thrownBambooJavelin;
    }

    public void releaseUsing(
        @NotNull ItemStack stack,
        @NotNull Level level,
        @NotNull LivingEntity entityLiving,
        int timeLeft) {
        if (entityLiving instanceof Player player) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (i >= 10) {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                if ((!(f > 0.0F) || player.isInWaterOrRain())) {
                    Holder<SoundEvent> holder = EnchantmentHelper
                            .pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND)
                            .orElse(SoundEvents.TRIDENT_THROW);
                    if (!level.isClientSide) {
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                        if (f == 0.0F) {
                            ThrownBambooJavelinEntity thrownBamboo = new ThrownBambooJavelinEntity(
                                ModEntities.THROWN_BAMBOO_JAVELIN.get(), player, level, stack, null);
                            thrownBamboo.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                            if (player.hasInfiniteMaterials()) {
                                thrownBamboo.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(thrownBamboo);
                            level.playSound(null, thrownBamboo, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!player.hasInfiniteMaterials()) {
                                player.getInventory().removeItem(stack);
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (f > 0.0F) {
                        float f7 = player.getYRot();
                        float f1 = player.getXRot();
                        float f2 = -Mth.sin(f7 * 0.017453292F) * Mth.cos(f1 * 0.017453292F);
                        float f3 = -Mth.sin(f1 * 0.017453292F);
                        float f4 = Mth.cos(f7 * 0.017453292F) * Mth.cos(f1 * 0.017453292F);
                        float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                        f2 *= f / f5;
                        f3 *= f / f5;
                        f4 *= f / f5;
                        player.push(f2, f3, f4);
                        player.startAutoSpinAttack(20, 8.0F, stack);
                        if (player.onGround()) {
                            player.move(MoverType.SELF, new Vec3(0.0, 1.1999999284744263, 0.0));
                        }

                        level.playSound(null, player, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }

    }


    public @NotNull InteractionResultHolder<ItemStack> use(
        @NotNull Level level,
        Player player,
        @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (EnchantmentHelper.getTridentSpinAttackStrength(itemstack, player) > 0.0F && !player.isInWaterOrRain()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        return true;
    }

    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public @NotNull DispenseConfig createDispenseConfig() {
        return ProjectileItem.super.createDispenseConfig();
    }

    @Override
    public void shoot(
        @NotNull Projectile projectile,
        double x, double y, double z,
        float velocity,
        float inaccuracy) {
        ProjectileItem.super.shoot(projectile, x, y, z, velocity, inaccuracy);
    }

    public static double getThrownBaseDamage(ItemStack pickupItemStack) {
        double x = 1.0 - (double) pickupItemStack.getDamageValue() / pickupItemStack.getMaxDamage();
        x = Math.min(1.0, x);
        double r = 1.5 * x;
        r = Math.max(r, 0.1);
        return r;
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_TRIDENT_ACTIONS.contains(itemAbility);
    }

}
