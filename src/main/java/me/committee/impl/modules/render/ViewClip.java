package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.render.BlockCameraEvent;

public class ViewClip extends Module {

    public ViewClip() {
        super("ViewClip", new String[]{""}, "lets your camera pass through blocks", Category.RENDER);
    }

    @EventSubscribe
    public void onBlockCamera(BlockCameraEvent event){
        event.cancel();
    }
}
