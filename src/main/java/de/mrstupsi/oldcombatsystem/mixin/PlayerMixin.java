package de.mrstupsi.oldcombatsystem.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, priority = 500)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract float getAttackStrengthScale(float p_36404_);

    @Shadow public abstract void crit(Entity p_36156_);

    @Shadow public abstract void magicCrit(Entity p_36253_);

    @Shadow public abstract void awardStat(Stat<?> p_36247_);

    @Shadow public abstract void causeFoodExhaustion(float p_36400_);

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At(value = "HEAD"), method = "getCurrentItemAttackStrengthDelay", cancellable = true)
    public void getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> ci) {
        ci.cancel();
        ci.setReturnValue(0.0F);
    }

    /**
     * @author
     * @reason
     */
    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    public void attack(Entity entity, CallbackInfo ci) {
        ci.cancel();
        Player self = (Player) ((Object) this);
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(self, entity)) return;
        if (entity.isAttackable()) {
            if (!entity.skipAttackInteraction(self)) {
                float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float f1;
                if (entity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entity).getMobType());
                } else {
                    f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), MobType.UNDEFINED);
                }

                float f2 = this.getAttackStrengthScale(0.5F);
                f *= 0.2F + f2 * f2 * 0.8F;
                f1 *= f2;
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    float i = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK); // Forge: Initialize this value to the attack knockback attribute of the player, which is by default 0
                    i += EnchantmentHelper.getKnockbackBonus(this);
                    boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.onClimbable() && !this.isInWater() && !this.hasEffect(MobEffects.BLINDNESS) && !this.isPassenger() && entity instanceof LivingEntity;
                    flag2 = flag2 && !this.isSprinting();
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(self, entity, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2) {
                        f *= hitResult.getDamageModifier();
                    }
                    f += f1;
                    boolean flag3 = false;
                    double d0 = (double)(this.walkDist - this.walkDistO);
                    if (flag && !flag2 && !flag1 && this.onGround && d0 < (double)this.getSpeed()) {
                        ItemStack itemstack = this.getItemInHand(InteractionHand.MAIN_HAND);
                        flag3 = itemstack.canPerformAction(net.minecraftforge.common.ToolActions.SWORD_SWEEP);
                    }
                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspect(this);
                    if (entity instanceof LivingEntity) {
                        f4 = ((LivingEntity)entity).getHealth();
                        if (j > 0 && !entity.isOnFire()) {
                            flag4 = true;
                            entity.setSecondsOnFire(1);
                        }
                    }
                    Vec3 vec3 = entity.getDeltaMovement();
                    boolean flag5 = entity.hurt(DamageSource.playerAttack(self), f);
                    if (flag5) {
                        if (i > 0) {
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).knockback((double)((float)i * 0.5F), (double) Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                            } else {
                                entity.push((double) (-Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F));
                            }
                            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                            this.setSprinting(false);
                        }
                        if (entity instanceof ServerPlayer && entity.hurtMarked) {
                            ((ServerPlayer)entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
                            entity.hurtMarked = false;
                            entity.setDeltaMovement(vec3);
                        }
                        if (flag2) crit(entity);
                        if (f1 > 0.0F) magicCrit(entity);
                        this.setLastHurtMob(entity);
                        if (entity instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity)entity, this);
                        }
                        EnchantmentHelper.doPostDamageEffects(this, entity);
                        ItemStack itemstack1 = this.getMainHandItem();
                        if (entity instanceof net.minecraftforge.entity.PartEntity) {
                            entity = ((net.minecraftforge.entity.PartEntity<?>) entity).getParent();
                        }
                        if (!this.level.isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hurtEnemy((LivingEntity) entity, self);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(self, copy, InteractionHand.MAIN_HAND);
                                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }
                        if (entity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity) entity).getHealth();
                            self.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                entity.setSecondsOnFire(j * 4);
                            }

                            if (this.level instanceof ServerLevel && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerLevel)this.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(0.5D), entity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }
                        this.causeFoodExhaustion(0.1F);
                    } else {
                        if (flag4) entity.clearFire();
                    }
                }
            }
        }
    }
}