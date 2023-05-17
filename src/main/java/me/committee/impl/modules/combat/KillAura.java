package me.committee.impl.modules.combat;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.Comparator;


public class KillAura extends Module {

    private final Setting<Boolean> weaponCheck = new Setting<>("Weapon Check", new String[]{"Wapen bekijken"}, "Checks if you are holding a weapon.", true);
    private final Setting<Boolean> players = new Setting<>("Players", new String[]{"Speler"}, "Hits players.", true);
    private final Setting<Boolean> friends = new Setting<>("Friends", new String[]{"Vriend"}, "If you want to attack your friends for some reason.", false);
    private final Setting<Boolean> enemy = new Setting<>("EnemyPriority", new String[]{"Vijhand"}, "If you don't want to prioretise your enemies.", true);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", new String[]{"Monster"}, "Hits hostile mobs.", false);
    private final Setting<Boolean> animals = new Setting<>("Animals", new String[]{"Dier"}, "Hits friendly mobs.", false);

    public Setting<Float> reach = new Setting<>("Reach", new String[]{"Berijk", "Range"}, "Range to hit entities.", 3.5f, 0.5f, 8.0f, 0.1f);

    public KillAura() {
        super("KillAura", new String[]{"MoordAura"}, "Automatically attacks entities that get close to the player.", Category.COMBAT);
    }

    @EventSubscribe
    public void onUpdate(PlayerUpdateEvent event) {
        final Item itemHeld = mc.player.getHeldItemMainhand().getItem();
        if (weaponCheck.getValue() && !(itemHeld instanceof ItemSword || itemHeld instanceof ItemAxe))
            return;
        if (mc.player.getCooledAttackStrength(0) < 1)
            return;
        final Entity closest = this.getClosestEntity();
        if (closest != null) {
            mc.player.connection.sendPacket(new CPacketUseEntity(closest));
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.resetCooldown();
        }
    }


    private Entity getClosestEntity() {
        final ArrayList<EntityPlayer> enemies = new ArrayList<>();
        final ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity : mc.world.loadedEntityList) {
            if (!this.isValidEntity(entity))
                continue;

            if (entity.getDistance(mc.player) > reach.getValue())
                continue;

            if (enemy.getValue() && entity instanceof EntityPlayer && Committee.playerManager.isEnemy(entity.getName()))
                enemies.add((EntityPlayer) entity);

            entities.add(entity);
        }

        return (enemies.isEmpty() ? entities : enemies).stream().min(Comparator.comparingDouble(entity -> entity.getDistance(mc.player))).orElse(null);
    }

    private boolean isValidEntity(Entity entity) {
        if (players.getValue() && entity instanceof EntityPlayer && entity != mc.player) { // Checks if the player's setting is enabled, the entity is a player, and it is not the user
            if (Committee.playerManager.isFriend(entity.getName())) { // If they are a friend
                return friends.getValue(); // If friend's setting is on return true (hit them) else if the setting is off return false (don't hit them)
            }
            return true; // hit them
        } else if (mobs.getValue() && entity instanceof EntityAmbientCreature) { // Checks if the mob setting is on and it is a Mob
            return true; // hit them
        } else if (animals.getValue() && entity instanceof EntityAnimal) { // Checks if the animals setting is on and it is an Animal
            return true; // hit them
        }
        return false; // don't hit them

    }
}


