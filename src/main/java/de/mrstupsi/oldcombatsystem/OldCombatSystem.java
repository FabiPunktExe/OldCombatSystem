package de.mrstupsi.oldcombatsystem;

import de.mrstupsi.oldcombatsystem.listener.LivingEntityUseItemListener;
import de.mrstupsi.oldcombatsystem.listener.LivingKnockBackListener;
import de.mrstupsi.oldcombatsystem.listener.PlayerInteractListener;
import de.mrstupsi.oldcombatsystem.listener.ShieldBlockListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("oldcombatsystem")
public class OldCombatSystem {
    public OldCombatSystem() {
        MinecraftForge.EVENT_BUS.register(new LivingEntityUseItemListener());
        MinecraftForge.EVENT_BUS.register(new LivingKnockBackListener());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractListener());
        MinecraftForge.EVENT_BUS.register(new ShieldBlockListener());
    }
}
