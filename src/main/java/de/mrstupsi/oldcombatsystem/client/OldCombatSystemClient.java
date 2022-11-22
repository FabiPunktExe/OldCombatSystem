package de.mrstupsi.oldcombatsystem.client;

import de.mrstupsi.oldcombatsystem.client.listener.RenderGuiListener;
import de.mrstupsi.oldcombatsystem.client.listener.RenderHandListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "oldcombatsystem", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OldCombatSystemClient {
    @SubscribeEvent
    public static void onSetup(final FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(new RenderGuiListener());
        MinecraftForge.EVENT_BUS.register(new RenderHandListener());
    }
}