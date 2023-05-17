package me.committee.impl.modules.combat;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MathUtil;
import me.committee.api.util.RenderUtil;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import me.committee.impl.event.events.render.Render3DEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class AutoCrystal extends Module {

    // todo: continue this, currently idk can't be bothered
    public AutoCrystal() {
        super("AutoCrystal", new String[]{"CrystalAura"}, "Places and breaks crystals automatically.", Category.COMBAT, true);
    }

    private final Setting<Float> maxRange = new Setting<>("MaxRange", new String[]{}, "How far can entities be from the place position for it to be calculated", 13f, 0f, 20f, 0.1f);

    private BlockPos lastBlock = null;

    @EventSubscribe
    public void onFrame(PlayerUpdateEvent event) {

        HashMap<Entity, DamageHolder> damageMap = new HashMap<>();
        double bestDamage = 0;
        BlockPos bestDamagePosition = null;

        for (Entity entity : mc.world.loadedEntityList) {
            for (int x = -7; x < 7; x++) {
                for (int z = -7; z < 7; z++) {
                    for (int y = -7; y < 7; y++) {
                        final BlockPos placePos = mc.player.getPosition().add(x, y, z);
                        if (entity.getDistance(placePos.getX(), placePos.getY(), placePos.getZ()) > maxRange.getValue()) {
                            continue;
                        }

                        double newdmg = MathUtil.getDamage(new Vec3d(mc.player.getPosition().getX() + x, mc.player.getPosition().getY() + y, mc.player.getPosition().getZ() + z), entity);

                        DamageHolder damageHolder = damageMap.get(entity);

                        if (damageHolder == null) {
                            continue;
                        }

                        if (newdmg > damageHolder.damage) {
                            damageHolder.damage = newdmg;
                            damageMap.put(entity, damageHolder);
                        }

                        if (bestDamage < newdmg) {
                            bestDamage = newdmg;
                            bestDamagePosition = placePos;
                        }
                    }
                }
            }
            bestDamage = 0;
        }

        if (bestDamagePosition != null) {
            lastBlock = new BlockPos(bestDamagePosition);
        }
    }

    @EventSubscribe
    public void onRender3d(Render3DEvent event) {
        if (lastBlock != null) {
            RenderUtil.drawBoundingBox(lastBlock.getX(), lastBlock.getY(), lastBlock.getZ(), lastBlock.getX() + 1, lastBlock.getY() + 1, lastBlock.getZ() + 1, 0xffffff00);
        }
        // todo: render on last black
    }


    static class DamageHolder {

        public Vec3d vec3d;
        public double damage;

        public DamageHolder(Vec3d vec3d, double damage) {
            this.vec3d = vec3d;
            this.damage = damage;
        }
    }
}
