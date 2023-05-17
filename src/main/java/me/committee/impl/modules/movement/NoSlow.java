package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.blocks.SoulSandCollisionEvent;
import me.committee.impl.event.events.blocks.WebCollisionEvent;
import me.committee.impl.event.events.player.HandActiveSlowdownEvent;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.init.Blocks;

public class NoSlow extends Module {

    private final Setting<Boolean> food = new Setting<>("Food", new String[]{"F"}, "Allows you to move at a normal speed while hand is active.", true);
    private final Setting<Boolean> soulSand = new Setting<>("SoulSand", new String[]{"Soul", "Sand"}, "Allows you to move at a normal speed while walking on soul sand.", true);
    private final Setting<Boolean> webs = new Setting<>("Webs", new String[]{"CobWebs", "SpooderWebs"}, "Allows you to move at a normal speed while walking in cobwebs.", false);
    private final Setting<Boolean> ice = new Setting<>("Ice", new String[]{"NoSlippySolidWater"}, "Allows you to move at a normal speed while walking on ice.", false);

    public NoSlow() {
        super("NoSlow", new String[]{"NoSlowDown"}, "No slow when using items.", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        Blocks.ICE.setDefaultSlipperiness(0.98f);
        Blocks.FROSTED_ICE.setDefaultSlipperiness(0.98f);
        Blocks.PACKED_ICE.setDefaultSlipperiness(0.98f);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (ice.getValue()) {
            Blocks.ICE.setDefaultSlipperiness(0.6f);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.6f);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.6f);
        } else {
            Blocks.ICE.setDefaultSlipperiness(0.98f);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.98f);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.98f);
        }
    }

    @EventSubscribe
    public void onHandActiveSlowdown(HandActiveSlowdownEvent event) {
        if (food.getValue()) event.cancel();
    }

    @EventSubscribe
    public void onSoulSandCollision(SoulSandCollisionEvent event) {
        if (soulSand.getValue()) event.cancel();
    }

    @EventSubscribe
    public void onWebCollision(WebCollisionEvent event) {
        if (webs.getValue()) event.cancel();
    }
}


