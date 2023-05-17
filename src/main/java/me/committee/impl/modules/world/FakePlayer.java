package me.committee.impl.modules.world;

import  me.committee.api.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.EnumHand;
import me.committee.api.setting.Setting;

import com.mojang.authlib.GameProfile;

import java.util.Random;
import java.util.UUID;

// Made By Pliv, Trans Rights NYA~
public class FakePlayer extends Module {

    private final Setting<Boolean> copyInv = new Setting<>("CopyInventory", new String[]{"KopierInv"}, "Copies players inventory (for armor).", false);
    private final Setting<String> name = new Setting<>("Name", new String[]{}, "The name for the fake player", "Fit");

    private EntityOtherPlayerMP otherPlayer;

    private final Random random = new Random();

    public FakePlayer() {
        super("FakePlayer", new String[]{"NepSpeler"}, "Creates a second player for you to test modules on.", Category.WORLD);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player != null  && mc.world != null) {
            otherPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.randomUUID(), name.getValue()));
            otherPlayer.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.cameraYaw, mc.player.cameraPitch);
            if (copyInv.getValue()){
                otherPlayer.inventory.copyInventory(mc.player.inventory);
            } else {
                otherPlayer.setHeldItem(EnumHand.MAIN_HAND,mc.player.getHeldItemMainhand());
            }
            mc.world.addEntityToWorld(random.nextInt(), otherPlayer);
        }
    }
    

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.world != null && otherPlayer != null) {
            mc.world.removeEntity(otherPlayer);
        }
    }       
    
    

}