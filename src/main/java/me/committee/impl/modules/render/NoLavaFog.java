package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.render.RenderLavaFogEvent;

public class NoLavaFog extends Module {

    private final Setting<Float> density = new Setting<>("fogdensity", "how dense the fog is", 0f, 0f, 5f, 0.01f);

    public NoLavaFog() {
        super("FogDensity", new String[]{""}, "lets u see shit when ur under lava", Category.RENDER);
    }

    @EventSubscribe
    public void onRenderLavaFog(RenderLavaFogEvent event){
        event.setDensity(density.getValue());
    }
}
