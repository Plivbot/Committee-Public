package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", new String[]{""}, "Automatically respawns on death.", Category.MISC);
    }


    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mc.player == null)
            return;

        if (mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }


}
