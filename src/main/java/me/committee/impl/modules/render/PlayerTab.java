package me.committee.impl.modules.render;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.render.TabOverlayPlayerEvent;

public class PlayerTab extends Module {

    public PlayerTab() {
        super("PlayerTab", new String[]{"Tab", "Players"}, "Renders friends and enemies in different colours on the TabOverlay menu.", Category.RENDER);
    }

    @EventSubscribe
    public void onTabOverlayPlayer(TabOverlayPlayerEvent event) {
        event.cancel();
        if (event.getName().equals(mc.player.getName()))
            event.setName("\u00a76" + event.getName());
        else if (Committee.playerManager.isFriend(event.getName()))
            event.setName("\u00a72" + event.getName());
        else if (Committee.playerManager.isEnemy(event.getName()))
            event.setName("\u00a7c" + event.getName());
        else
            event.setCancelled(false);
    }

}
