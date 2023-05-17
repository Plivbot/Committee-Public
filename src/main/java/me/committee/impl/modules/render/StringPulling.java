package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.render.FishLineColourEvent;
import me.committee.impl.event.events.render.RenderFishLineEvent;
import net.minecraft.client.renderer.GlStateManager;

public class StringPulling extends Module {

    private final Setting<Integer> width = new Setting<>("Width", new String[]{"W", "Size"}, "The width of the fishing line.", 2, 1, 10, 1);
    private final Setting<Colour> colour = new Setting<>("Colour", new String[]{}, "The colour of the fishing line.", new Colour(255, 255, 255, 255));

    public StringPulling() {
        super("StringPulling", new String[]{"StringPull", "PullStrings", "PullString", "AvgCommitteeMemberActivity"}, "Changes how rods look for Committee images.", Category.RENDER);
    }

    @EventSubscribe
    public void onRenderFishLine(RenderFishLineEvent event) {
        GlStateManager.glLineWidth(width.getValue());
    }

    @EventSubscribe
    public void onFishLineColour(FishLineColourEvent event) {
        event.cancel();
        event.getColour().setAll(colour.getValue());
    }

}
