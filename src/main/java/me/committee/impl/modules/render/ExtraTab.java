package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.render.RenderTabMenuEvent;

public class ExtraTab extends Module {

    public ExtraTab() {
        super("ExtraTab", new String[]{""}, "makes tab menu show more players", Category.RENDER);
    }

    @EventSubscribe
    public void renderTabMenu(RenderTabMenuEvent event) {
        event.cancel();
    }

}
