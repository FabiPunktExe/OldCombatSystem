package de.mrstupsi.oldcombatsystem.listener;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerInteractListener {
    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
        Player p = e.getEntity();
        if (e.getItemStack().getItem() instanceof SwordItem) {
            p.startUsingItem(e.getHand());
            e.setCancellationResult(InteractionResult.CONSUME_PARTIAL);
            e.setCanceled(true);
        }
    }
}