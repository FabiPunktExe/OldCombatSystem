package de.mrstupsi.oldcombatsystem.listener;

import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShieldBlockListener {
    @SubscribeEvent
    public void onBlock(ShieldBlockEvent e) {
        e.setShieldTakesDamage(false);
        e.setBlockedDamage(e.getBlockedDamage() / 2);
    }
}