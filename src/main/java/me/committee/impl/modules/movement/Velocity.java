package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.player.EntityCollisionEvent;
import me.committee.impl.event.events.player.PushOutOfBlocksEvent;
import me.committee.impl.event.events.player.PushedByWaterEvent;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import me.committee.api.setting.Setting;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import me.committee.impl.event.events.network.ReceivePacketEvent;


public class Velocity extends Module {

    private final Setting<Boolean> explosion = new Setting<>("Explosion", "Stops explosion velocity.", true);
    private final Setting<Boolean> bobber = new Setting<>("FishingRod", "Stops you from getting pulled by a fishing rod.", false);
    private final Setting<Boolean> noPush = new Setting<>("NoPush", "Stops you from getting pushed.", false);
    private final Setting<Boolean> noWaterPush = new Setting<>("Water","Stops you from floating away in water.", false);

    public Velocity() {super("Velocity", new String[]{"AntiKnockback", "NoPush", "GeenTerugslag"}, "Stops velocity.", Category.MOVEMENT);
    }

    @EventSubscribe
    public void onPacketRecieve(ReceivePacketEvent event) {
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
            if (packet.getEntityID() == mc.player.getEntityId()) {
                event.cancel();
            }
        } else if (event.getPacket() instanceof SPacketExplosion && explosion.getValue()) {
            event.cancel();
        } else if (event.getPacket() instanceof SPacketEntityStatus && bobber.getValue()) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 31) {
                event.cancel();
            }
        }
    }

    @EventSubscribe
    public void onPushOutOfBlocks(PushOutOfBlocksEvent event) {
        if (noPush.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onEntityCollision(EntityCollisionEvent event) {
        if (noPush.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onPushedByWater(PushedByWaterEvent event) {
        if (noWaterPush.getValue())
            event.cancel();

    }

}
