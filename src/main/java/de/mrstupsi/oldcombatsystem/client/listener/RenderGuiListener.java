package de.mrstupsi.oldcombatsystem.client.listener;

import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderGuiListener {
    private AttackIndicatorStatus attackIndicator;

    @SubscribeEvent
    public void onRenderPre(RenderGuiEvent.Pre e) {
        attackIndicator = Minecraft.getInstance().options.attackIndicator().get();
        Minecraft.getInstance().options.attackIndicator().set(AttackIndicatorStatus.OFF);
    }

    @SubscribeEvent
    public void onRenderPost(RenderGuiEvent.Post e) {
        Minecraft.getInstance().options.attackIndicator().set(attackIndicator);
    }
}