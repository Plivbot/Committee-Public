package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MessageSendHelper;
import me.committee.impl.event.events.input.MoveEvent;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.init.MobEffects;

import java.util.Objects;

public class Strafe extends Module {

    private final Setting<Boolean> jump = new Setting<>("Jump", new String[] {"Jump", "Spring"}, "Jumps Automatically", false );

    public Strafe() {
        super("Strafe", new String[]{"AirStrafe"}, "Movement in air.", Category.MOVEMENT);
    }

    private float playerSpeed;

    @Override
    public void onEnable() {
        super.onEnable();
        this.playerSpeed = 0.2873f;
    }

    @EventSubscribe
    public void onMove(MoveEvent event) {
        if (mc.player.isElytraFlying())
            return;

        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.rotationYaw;

        if (jump.getValue()) {
            if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
                if (mc.player.onGround) {
                    mc.player.motionY = 0.40123128;
                    event.setY(0.40123128);
                    this.playerSpeed = 0.3873f;
                } else {
                    this.playerSpeed = Math.max(playerSpeed * 0.99f, 0.35f);
                }
            }
        } else {
            if (mc.player.onGround) {
                this.playerSpeed = 0.2873f;
            }
        }

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            playerSpeed *= (1.0f + 0.2f * (amplifier + 1));
        }

        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.setX(0.0d);
            event.setZ(0.0d);
        } else {
            if (moveForward != 0.0f) {
                if (moveStrafe > 0.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
                } else if (moveStrafe < 0.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
                }
                moveStrafe = 0.0f;
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                } else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            double sin = Math.sin(Math.toRadians((rotationYaw + 90.0f)));
            double cos = Math.cos(Math.toRadians((rotationYaw + 90.0f)));
            event.setX((moveForward * playerSpeed) * cos + (moveStrafe * playerSpeed) * sin);
            event.setZ((moveForward * playerSpeed) * sin - (moveStrafe * playerSpeed) * cos);
        }
    }
}
