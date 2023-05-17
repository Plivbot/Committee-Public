package me.committee.impl.modules.world;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.InventoryUtil;
import me.committee.impl.event.events.render.RenderEntityOverMouseEvent;
import net.minecraft.item.ItemTool;

public class NoEntityTrace extends Module {

    public NoEntityTrace() {
        super("NoEntityTrace", new String[]{""}, "lets u mine through entities", Category.WORLD); // Sets up the module
    }

    private final Setting<Boolean> tools = new Setting<>("Speed", "sped", true);

    @EventSubscribe
    public void onEntityOverCrosshair(RenderEntityOverMouseEvent event) {
        if (tools.getValue()) {
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof ItemTool) {
                event.cancel();
            }
        } else {
            event.cancel();
        }
    }
}
