package me.committee.impl.modules.world;

import com.mojang.authlib.GameProfile;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MathUtil;
import me.committee.impl.event.events.blocks.AddCollisionBoxEvent;
import me.committee.impl.event.events.input.MoveEvent;
import me.committee.impl.event.events.network.ReceivePacketEvent;
import me.committee.impl.event.events.network.SendPacketEvent;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import me.committee.impl.event.events.player.PlayerWalkingUpdateEvent;
import me.committee.impl.event.events.player.PushOutOfBlocksEvent;
import me.committee.impl.event.events.render.SuffocationOverlayEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class Freecam extends Module {

    private final Setting<Float> speed = new Setting<>("Speed", "sped", 1f, 0.1f, 5f, 0.1f);
    //private final Setting<Float> verticalSpeed = new Setting<>("VerticalSpeed", "sped but vertical", 5f, 0.1f, 10f, 0.1f);


    public Freecam() {
        super("Freecam", new String[]{""}, "move around n shit", Category.WORLD);
    }

    private EntityOtherPlayerMP playerCopy;


    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player != null && mc.world != null) {
            playerCopy = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), mc.player.getName()));
            playerCopy.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.cameraYaw, mc.player.cameraPitch);
            mc.world.addEntityToWorld(69696969, playerCopy);
            mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setPosition(playerCopy.posX, playerCopy.posY, playerCopy.posZ);
        mc.world.removeEntity(playerCopy);
        mc.player.noClip = false;
        mc.player.motionX = 0;
        mc.player.motionY = 0;
        mc.player.motionZ = 0;
    }

    @EventSubscribe
    public void onSendPacket(SendPacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (!(packet instanceof CPacketPlayerTryUseItemOnBlock) && !(packet instanceof CPacketPlayerTryUseItem) && !(packet instanceof CPacketChatMessage) && !(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketPlayerDigging) && !(packet instanceof CPacketHeldItemChange) && !(packet instanceof CPacketClickWindow) && !(packet instanceof CPacketCloseWindow)) {
            event.setCancelled(true);
        }
    }

    @EventSubscribe
    public void update(MoveEvent event) {
        mc.player.noClip = true;
    }

    @EventSubscribe
    public void onUpdate(PlayerWalkingUpdateEvent event) {

        mc.player.setVelocity(0, 0, 0);

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0) {
            mc.player.motionX = MathUtil.directionSpeed(speed.getValue())[0];
            mc.player.motionZ = MathUtil.directionSpeed(speed.getValue())[1];
        }

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = speed.getValue();
        }

        if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.player.motionY = speed.getValue() - speed.getValue() * 2;

    }

    @EventSubscribe
    public void onRecievePacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect sound = (SPacketSoundEffect) event.getPacket();
            if (sound.getSound() == SoundEvents.ENTITY_PLAYER_BIG_FALL || sound.getSound() == SoundEvents.ENTITY_PLAYER_SMALL_FALL)
                event.cancel();
        }
    }

    @EventSubscribe
    public void onPushOutOfBlocks(PushOutOfBlocksEvent event) {
        event.cancel();
    }

    @EventSubscribe
    public void onCollide(AddCollisionBoxEvent event) {
        event.cancel();
    }

    @EventSubscribe
    public void onSuffocationOverlay(SuffocationOverlayEvent event) {
        event.cancel();
    }
}
