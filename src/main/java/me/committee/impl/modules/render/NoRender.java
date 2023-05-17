package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MessageSendHelper;
import me.committee.impl.event.events.network.ReceivePacketEvent;
import me.committee.impl.event.events.render.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumParticleTypes;

public class NoRender extends Module {

    public NoRender() {
        super("NoRender", new String[]{"AntiLag"}, "Module that helps with reducing render based lag and exploits", Category.RENDER);
    }

    private final Setting<Boolean> fireworks = new Setting<>("Fireworks", "Disables rendering fireworks.", true);
    private final Setting<Boolean> enchantmentTables = new Setting<>("EnchantmentTables", "Disables rendering enchantment table books.", true);
    private final Setting<Boolean> witherSkulls = new Setting<>("WitherSkulls", "Disables rendering wither skulls.", false);
    private final Setting<Boolean> potions = new Setting<>("Potions", "removes the potion hud element", true);
    private final Setting<Boolean> portal = new Setting<>("Portal", "does not show portal overlay", true);
    private final Setting<Boolean> water = new Setting<>("Water", "hides water overlay and fog", true);
    private final Setting<Boolean> fire = new Setting<>("Fire", "hides fire", true);
    private final Setting<Boolean> hurtcam = new Setting<>("hurtcam", "stops hurtcam shake", true);

    private final Setting<NoArmourMode> armour = new Setting<>("Armour", new String[]{"Armor"}, "Disables rendering of armour.", NoArmourMode.NONE);
    private final Setting<NoParticlesMode> particles = new Setting<>("Particles", "Disables spawning of particles.", NoParticlesMode.NONE);


    @EventSubscribe
    public void onPacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            if (fireworks.getValue() && ((SPacketSpawnObject) event.getPacket()).getType() == 76)
                event.setCancelled(true);
            else if (witherSkulls.getValue() && ((SPacketSpawnObject) event.getPacket()).getType() == 66)
                event.setCancelled(true);
        }
    }

    @EventSubscribe
    public void onEnchantmentTableRender(EnchantmentBookRenderEvent event) {
        if (enchantmentTables.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onRenderPotions(RenderPotionsEvent event) {
        if (potions.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onRenderPortalOverlay(RenderPortalEvent event) {
        if (portal.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onRenderFire(FireOverlayEvent event){
        if (fire.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onRenderWater(WaterOverlayEvent event){
        if (water.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onRenderWaterFog(RenderWaterFogEvent event){
        if (water.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onHurtCamShake(RenderHurtCamEvent event) {
        if (hurtcam.getValue())
            event.cancel();
    }

    @EventSubscribe
    public void onRenderLayerArmour(RenderLayerArmourEvent event) {
        switch (this.armour.getValue()) {
            case ALL:
                event.cancel();
                break;
            case PLAYERS:
                if (event.getEntityLivingBase() instanceof EntityPlayer)
                    event.cancel();
                break;
        }
    }

    @EventSubscribe
    public void onSpawnParticle(SpawnParticleEvent event) {
        switch (this.particles.getValue()) {
            case ALL:
                event.cancel();
                break;
            case LIQUIDS:
                final int particleID = event.getParticleID();
                if (
                        particleID == EnumParticleTypes.WATER_WAKE.getParticleID() ||
                                particleID == EnumParticleTypes.WATER_BUBBLE.getParticleID() ||
                                particleID == EnumParticleTypes.WATER_DROP.getParticleID() ||
                                particleID == EnumParticleTypes.WATER_SPLASH.getParticleID() ||
                                particleID == EnumParticleTypes.LAVA.getParticleID() ||
                                particleID == EnumParticleTypes.DRIP_LAVA.getParticleID()
                )
                    event.cancel();
                break;
        }
    }

    private enum NoArmourMode {
        NONE, ALL, PLAYERS
    }

    private enum NoParticlesMode {
        NONE, ALL, LIQUIDS
    }

}
