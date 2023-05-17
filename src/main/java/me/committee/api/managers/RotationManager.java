package me.committee.api.managers;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorCPacketPlayer;
import me.committee.api.module.Module;
import me.committee.api.util.MathUtil;
import me.committee.impl.event.events.network.SendPacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RotationManager {

    public static final float MAX_YAW_DEG_PER_PACKET = 5;
    public static final float MAX_PITCH_DEG_PER_PACKET = 5;

    private float spoofedYaw;
    private float spoofedPitch;
    private boolean rotating = false;

    private final Minecraft mc = Minecraft.getMinecraft();

    private final Map<Module, Vec3d> priorityMap = new HashMap<>();

    /**
     * Updates what angles we want and returns do we want to rotate rn
     */
    public boolean updateWantedAngles() {

        if (getHighestPriorityTarget() == null) {
            return false;
        }

        float[] targetAngles = MathUtil.calculateLookAt(mc.player.getPositionVector().add(0f, mc.player.getEyeHeight(), 0f), getHighestPriorityTarget());

        if (Math.abs(spoofedYaw - targetAngles[0]) < MAX_YAW_DEG_PER_PACKET &&
                Math.abs(spoofedPitch - targetAngles[1]) < MAX_PITCH_DEG_PER_PACKET) {
            rotating = false;
            return false;
        }

        rotating = true;

        final float[] nextAngles = new float[]{
                targetAngles[0] > spoofedYaw ? spoofedYaw + MAX_YAW_DEG_PER_PACKET : spoofedYaw - MAX_YAW_DEG_PER_PACKET,
                targetAngles[1] > spoofedPitch ? spoofedPitch + MAX_PITCH_DEG_PER_PACKET : spoofedPitch - MAX_PITCH_DEG_PER_PACKET
        };

        spoofedYaw = nextAngles[0];
        spoofedPitch = nextAngles[1];

        return true;
    }

    /**
     * This should be called on player update in each module that wants to use rotations on that update
     */
    public void setTargetPosition(Module module, Vec3d target) {
        priorityMap.put(module, target);
    }

    private Vec3d getHighestPriorityTarget() {

        // sussy code
        AtomicReference<Vec3d> current = new AtomicReference<>();
        final int[] priority = {-1};
        priorityMap.forEach((module, vec3d) -> {
            if (module.getRotationPriority() > priority[0]) {
                current.set(vec3d);
                priority[0] = module.getRotationPriority();
            }
        });

        return current.get();
    }

    @EventSubscribe
    public void onPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketPlayer.PositionRotation || event.getPacket() instanceof CPacketPlayer.Rotation) {

            if (updateWantedAngles()) {
                ((AccessorCPacketPlayer) event.getPacket()).setYaw(spoofedYaw);
                ((AccessorCPacketPlayer) event.getPacket()).setPitch(spoofedPitch);
            }

            priorityMap.clear();
        }
    }

    public boolean isRotating() {
        return rotating;
    }
}