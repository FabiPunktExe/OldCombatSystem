package de.mrstupsi.oldcombatsystem;

import de.mrstupsi.oldcombatsystem.listener.EntityAttackListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("oldcombatsystem")
public class OldCombatSystem {
    public OldCombatSystem() {
        MinecraftForge.EVENT_BUS.register(new EntityAttackListener());
    }
}
