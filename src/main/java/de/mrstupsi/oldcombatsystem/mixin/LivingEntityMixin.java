package de.mrstupsi.oldcombatsystem.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow public abstract boolean isUsingItem();

    @Shadow protected ItemStack useItem;

    @Shadow protected int useItemRemaining;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isDamageSourceBlocked(DamageSource p_21276_) {
        Entity entity = p_21276_.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow abstractarrow) {
            if (abstractarrow.getPierceLevel() > 0) {
                flag = true;
            }
        }

        if (!p_21276_.isBypassArmor() && this.isBlocking() && !flag) {
            Vec3 vec32 = p_21276_.getSourcePosition();
            if (vec32 != null) {
                Vec3 vec3 = this.getViewVector(1.0F);
                Vec3 vec31 = vec32.vectorTo(this.position()).normalize();
                vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
                if (vec31.dot(vec3) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    @Inject(at = @At(value = "HEAD"), method = "hurt")
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (isBlocking()) amount *= 0.5;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isBlocking() {
        return isUsingItem() && useItem.getItem() instanceof SwordItem;
    }
}