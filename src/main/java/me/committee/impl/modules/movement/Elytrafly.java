package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorMinecraft;
import me.committee.api.mixin.mixins.accessors.AccessorTimer;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.PlayerUtil;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.client.CPacketEntityAction;

public class Elytrafly extends Module {

    private final Setting<Float> speed = new Setting<>("Speed", new String[]{"Spd"}, "Speed of flight to travel at.", 4.6f, 0.1f, 10f, 0.1f);
    private final Setting<Boolean> takeoff = new Setting<>("TakeOff", new String[]{"EasyTakeOff"}, "Allows you to take off easier by using timer on a low value.", true);

    private boolean shouldTimerReset = false;

    public Elytrafly() {
        super("Elytrafly", new String[]{"elyfly"}, "easy takeoff and flight", Category.MOVEMENT);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        mc.player.capabilities.setFlySpeed(speed.getValue() / 10);
        if (mc.player.isElytraFlying()) {
            this.shouldTimerReset = true;
            mc.player.capabilities.isFlying = true;
        }
        if (mc.player.fallDistance > 0 && mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isElytraFlying()) {
            if (takeoff.getValue()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            if (takeoff.getValue() && !mc.player.isInLava() && !mc.player.isInWater() && PlayerUtil.itemSlot(EntityEquipmentSlot.CHEST, Items.ELYTRA)) {
                ((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).setTickLength(500);
                this.shouldTimerReset = true;
            }
        } else if (this.shouldTimerReset) {
            ((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).setTickLength(50);
            this.shouldTimerReset = false;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).setTickLength(50);
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        this.shouldTimerReset = false;
    }
}
