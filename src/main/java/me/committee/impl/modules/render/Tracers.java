package me.committee.impl.modules.render;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorEntityRenderer;
import me.committee.api.mixin.mixins.accessors.AccessorRenderManager;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.setting.Setting;
import me.committee.api.util.MathUtil;
import me.committee.api.util.RenderUtil;
import me.committee.impl.event.events.render.Render3DEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {

    private final Setting<Boolean> players = new Setting<>("Players", new String[]{"players"}, "Display players on tracers.", true);
    private final Setting<Boolean> friends = new Setting<>("Friend", new String[]{"friend"}, "Display custom colour for friends on tracers.", true);
    private final Setting<Boolean> enemy = new Setting<>("Enemy", new String[]{"enemy"}, "Display custom colour for enemies on tracers.", true);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", new String[]{"mobs"}, "Display mobs on tracers.", true);
    private final Setting<Boolean> npcs = new Setting<>("NPCs", new String[]{"npc"}, "Display NPCs on tracers.", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", new String[]{"animals"}, "Display animals on tracers.", true);
    private final Setting<Boolean> crystals = new Setting<>("Crystals", new String[]{"crystals"}, "Display end crystals on tracers.", true);
    private final Setting<Colour> playerColour = new Setting<>("PlayerColour", new String[]{"PColour"}, "Colour of tracer line for players.", new Colour(0, 0, 0, 120));
    private final Setting<Colour> friendColour = new Setting<>("FriendColour", new String[]{"FColour"}, "Colour of tracer line for friends.", new Colour(84, 247, 247, 120));
    private final Setting<Colour> enemyColour = new Setting<>("EnemyColour", new String[]{"EColour"}, "Colour of tracer line for enemies.", new Colour(255, 0, 0, 120));
    private final Setting<Colour> mobColour = new Setting<>("MobColour", new String[]{"MColour"}, "Colour of tracer line for mobs.", new Colour(0, 0, 0, 120));
    private final Setting<Colour> npcColour = new Setting<>("NPCColour", new String[]{"NColour"}, "Colour of tracer line for NPCs.", new Colour(0, 0, 0, 120));
    private final Setting<Colour> animalColour = new Setting<>("AnimalColour", new String[]{"AColour"}, "Colour of tracer line for animals.", new Colour(0, 0, 0, 120));
    private final Setting<Colour> crystalColour = new Setting<>("CrystalColour", new String[]{"CColour"}, "Colour of tracer line for end crystals.", new Colour(0, 0, 0, 120));
    private final Setting<Float> lineWidth = new Setting<>("LineWidth", new String[]{"Width"}, "The width of the tracer lines.", 2.0f, 1.0f, 10.0f, 0.1f);

    public Tracers() {
        super("Tracers", new String[]{"Tracer"}, "Draws a line to entities.", Category.RENDER);
    }

    @EventSubscribe
    public void onRender3D(Render3DEvent event) {
        RenderUtil.startGL3D();

        final Vec3d playerEyeLocation = new Vec3d(0, 0, 1) // Gets the position on screen to
                .rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch))
                .rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));

        for (Entity entity : mc.world.loadedEntityList) {
            if (this.getColour(entity) == -1)
                continue;

            final int colour = this.getColour(entity);
            final Vec3d interpolatedPosition = MathUtil.getInterpolatedPosition(entity, event.getPartialTicks())
                    .subtract(
                            ((AccessorRenderManager) mc.getRenderManager()).getRenderPosX(),
                            ((AccessorRenderManager) mc.getRenderManager()).getRenderPosY(),
                            ((AccessorRenderManager) mc.getRenderManager()).getRenderPosZ()
                    );

            GlStateManager.pushMatrix();

            GL11.glLoadIdentity(); // Stops view bobbing affecting line positions
            ((AccessorEntityRenderer) mc.entityRenderer).cameraOrientation(event.getPartialTicks());

            GlStateManager.glLineWidth(lineWidth.getValue()); // Changes the thickness of the tracers

            RenderUtil.drawLine( // Draw the line from the players eye position to the entity position
                    playerEyeLocation.x,
                    playerEyeLocation.y + mc.player.getEyeHeight(),
                    playerEyeLocation.z,
                    interpolatedPosition.x,
                    interpolatedPosition.y,
                    interpolatedPosition.z,
                    colour
            );

            GlStateManager.popMatrix();
        }
        RenderUtil.endGL3D();
    }

    private int getColour(Entity e) {
        if (e instanceof EntityPlayer && e != mc.player && this.players.getValue()) {
            if (Committee.playerManager.isFriend(e.getName()) && this.friends.getValue())
                return this.friendColour.getValue().hashCode();
            else if (Committee.playerManager.isEnemy(e.getName()) && this.enemy.getValue())
                return this.enemyColour.getValue().hashCode();
            return this.playerColour.getValue().hashCode();
        } else if (e instanceof EntityAnimal && this.animals.getValue()) return this.animalColour.getValue().hashCode();
        else if (e instanceof EntityAnimal || e instanceof EntityWaterMob && this.mobs.getValue())
            return this.mobColour.getValue().hashCode();
        else if (e instanceof EntityVillager || e instanceof EntityIronGolem || e instanceof EntitySnowman && this.npcs.getValue())
            return this.npcColour.getValue().hashCode();
        else if (e instanceof EntityEnderCrystal && this.crystals.getValue())
            return this.crystalColour.getValue().hashCode();
        return -1;
    }

}
