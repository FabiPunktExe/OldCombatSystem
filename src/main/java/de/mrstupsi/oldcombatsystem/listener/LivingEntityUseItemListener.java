package de.mrstupsi.oldcombatsystem.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingEntityUseItemListener {
    @SubscribeEvent
    public void onItemUseStart(LivingEntityUseItemEvent.Start e) {
        if (e.getEntity() instanceof Player && e.getItem().getItem() instanceof SwordItem) {
            e.setDuration(72000);
        }
    }
}