package de.mrstupsi.oldcombatsystem.listener;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingKnockBackListener {
    @SubscribeEvent
    public void onLivingKnockBack(final LivingKnockBackEvent e) {
        if (e.getEntity() instanceof Player player && player.isBlocking()) {
            e.setStrength(e.getStrength() * 0.5F);
        }
        if (!e.getEntity().isOnGround() && !e.getEntity().isInWater()) {
            float strength = e.getStrength();
            strength *= 1.0 - e.getEntity().getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            final Vec3 deltaMovement = e.getEntity().getDeltaMovement();
            e.getEntity().setDeltaMovement(deltaMovement.x, Math.min(0.4, deltaMovement.y / 2.0D + strength), deltaMovement.x);
        }
    }
}