package me.committee.impl.modules.render;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.setting.Setting;
import me.committee.api.util.*;
import me.committee.impl.event.events.render.*;
import me.committee.impl.shaders.OutlineShader;
import me.committee.impl.shaders.SpaceShader;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ESP extends Module {

    private final Setting<RenderMode> mode = new Setting<>("Mode", new String[]{"RenderMode", "M"}, "The type of ESP to use.", RenderMode.SHADER);
    private final Setting<ShaderMode> shaderMode = new Setting<>("ShaderMode", new String[]{"Shader", "S"}, "The type of Shader to use.", ShaderMode.OUTLINE);
    private final Setting<ImageMode> imageMode = new Setting<>("ImageMode", new String[]{"Image", "I"}, "Image to use for ImageESP..", ImageMode.TITTY);

    private final Setting<Boolean> players = new Setting<>("Players", new String[]{"players"}, "Display players with ESP.", true);
    private final Setting<Boolean> friends = new Setting<>("Friends", new String[]{"friend"}, "Display custom colours for friends with ESP.", true);
    private final Setting<Boolean> enemy = new Setting<>("Enemies", new String[]{"enemy"}, "Display custom colours for enemies with ESP.", true);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", new String[]{"mobs"}, "Display mobs with ESP.", true);
    private final Setting<Boolean> npcs = new Setting<>("NPCs", new String[]{"npc"}, "Display NPCs with ESP.", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", new String[]{"animals"}, "Display animals with ESP.", true);
    private final Setting<Boolean> crystals = new Setting<>("Crystals", new String[]{"crystals"}, "Display end crystals with ESP.", true);
    private final Setting<Colour> playerColour = new Setting<>("PlayerColour", new String[]{"PColour"}, "Colour of ESP for players.", new Colour(255, 255, 255, 32));
    private final Setting<Colour> friendColour = new Setting<>("FriendColour", new String[]{"FColour"}, "Colour of ESP for friends.", new Colour(84, 247, 247, 32));
    private final Setting<Colour> enemyColour = new Setting<>("EnemyColour", new String[]{"EColour"}, "Colour of ESP for enemies.", new Colour(255, 0, 0, 32));
    private final Setting<Colour> entityColour = new Setting<>("EntityColour", new String[]{"NonPlayerColour"}, "Colour of ESP for entities.", new Colour(255, 255, 255, 32));
    private final Setting<Float> outlineRadius = new Setting<>("OutlineRadius", new String[]{"ShaderRadius"}, "The width of the shader outline.", 1.0f, 1.0f, 5.0f, 0.1f);
    private final Setting<Boolean> notSeen = new Setting<>("NotSeen", new String[]{"OnlyNotSeen"}, "Only renders ESP when the entity is not able to be seen.", false);

    private final ResourceLocation tittyImage = new ResourceLocation("minecraft", "titty_wolf_girl.jpg");
    private final ResourceLocation misakaImage = new ResourceLocation("minecraft", "misaka.png");


    private static ICamera viewCamera = new Frustum();

    private OutlineShader outlineShader;
    private SpaceShader spaceShader;
    private Framebuffer playerFramebuffer;
    private Framebuffer friendFramebuffer;
    private Framebuffer enemyFramebuffer;
    private Framebuffer entityFramebuffer;

    private boolean cancelNameRender = false;

    public ESP() {
        super("ESP", new String[]{"Glowing"}, "See objects through walls.", Category.RENDER);
    }

    @EventSubscribe
    public void onRender2D(Render2DEvent event) {
        if (mode.getValue() == RenderMode.SHADER) { // TODO: Do colouring for each entity type, do each entity type, more shaders?,
            this.cancelNameRender = true;
            if (this.outlineShader == null)
                this.outlineShader = new OutlineShader();

            if (this.spaceShader == null)
                this.spaceShader = new SpaceShader();

            this.getAndCreateFrameBuffers();

            final Set<Entity> entities = mc.world.loadedEntityList
                    .stream()
                    .filter(entity -> this.getColour(entity) != null)
                    .collect(Collectors.toSet());

            final Set<Entity> players = entities.stream().filter(entity -> entity instanceof EntityPlayer).collect(Collectors.toSet());

            if (this.friends.getValue()) {
                GlStateManager.enableAlpha();
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                this.friendFramebuffer.bindFramebuffer(true);

                Set<Entity> friends = players.stream().filter(entity -> Committee.playerManager.isFriend(entity.getName())).collect(Collectors.toSet());

                players.removeAll(friends);
                entities.removeAll(friends);

                this.renderShader(this.friendFramebuffer, event.getPartialTicks(), friends, friendColour.getValue());

                GlStateManager.popMatrix();
                GlStateManager.popAttrib();
            }

            if (this.enemy.getValue()) {
                GlStateManager.enableAlpha();
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                this.enemyFramebuffer.bindFramebuffer(true);

                Set<Entity> enemies = players.stream().filter(entity -> Committee.playerManager.isEnemy(entity.getName())).collect(Collectors.toSet());

                players.removeAll(enemies);
                entities.removeAll(enemies);

                this.renderShader(this.enemyFramebuffer, event.getPartialTicks(), enemies, enemyColour.getValue());
                GlStateManager.popMatrix();
                GlStateManager.popAttrib();
            }

            if (this.players.getValue()) {
                GlStateManager.enableAlpha();
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                this.playerFramebuffer.bindFramebuffer(true);

                entities.removeAll(players);

                this.renderShader(this.playerFramebuffer, event.getPartialTicks(), players, playerColour.getValue());
                GlStateManager.popMatrix();
                GlStateManager.popAttrib();
            }

            GlStateManager.enableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();

            this.entityFramebuffer.bindFramebuffer(true);

            this.renderShader(this.entityFramebuffer, event.getPartialTicks(), entities, entityColour.getValue());
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
            this.cancelNameRender = false;
        }
        else if (mode.getValue() == RenderMode.IMAGE) {
            final List<Entity> entities = mc.world.loadedEntityList
                    .stream()
                    .filter(entity -> this.getColour(entity) != null)
                    .collect(Collectors.toList());

            for (Entity entity : entities) {

                final double viewerX = mc.getRenderManager().viewerPosX;
                final double viewerY = mc.getRenderManager().viewerPosY;
                final double viewerZ = mc.getRenderManager().viewerPosZ;

                final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(0.3f, 1.0f, 0.3f);

                viewCamera.setPosition(viewerX, viewerY, viewerZ);

                if (!viewCamera.isBoundingBoxInFrustum(axisAlignedBB))
                    continue;

                final Vec3d interpPos = MathUtil.getInterpolatedPosition(entity, event.getPartialTicks());
                final Vec3d topPos = interpPos.add(0, entity.height, 0);

                final VectorUtils.ScreenLocation top = VectorUtils.toScreenPos(topPos);
                final VectorUtils.ScreenLocation bottom = VectorUtils.toScreenPos(interpPos);

                if (top.isShown() || bottom.isShown()) {
                    final double height = bottom.getY() - top.getY();
                    final double width = height;

                    final double x = top.getX() - (width / 1.8);
                    final double y = top.getY();

                    if (this.imageMode.getValue() == ImageMode.TITTY) mc.renderEngine.bindTexture(tittyImage);
                    else mc.renderEngine.bindTexture(misakaImage);


                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();

                    Gui.drawScaledCustomSizeModalRect((int) x, (int) y, (float) 0, (float) 0, (int) width, (int) height, (int) width, (int) height, (float) width, (float) height);

                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @EventSubscribe
    public void onRender3D(Render3DEvent event) {
        if (mode.getValue() == RenderMode.BOX) {
            RenderUtil.startGL3D();

            for (Entity entity : mc.world.loadedEntityList) {
                final Colour colour = this.getColour(entity);
                if (colour == null)
                    continue;

                final Vec3d renderPosition = MathUtil.getRenderTranslation(entity, event.getPartialTicks());

                GlStateManager.pushMatrix();

                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
                GlStateManager.translate(
                        renderPosition.x,
                        renderPosition.y,
                        renderPosition.z
                );

                RenderUtil.drawBoundingBox(
                        axisalignedbb.minX,
                        axisalignedbb.minY,
                        axisalignedbb.minZ,
                        axisalignedbb.maxX,
                        axisalignedbb.maxY,
                        axisalignedbb.maxZ,
                        colour.hashCode()
                );
                RenderUtil.drawFilledBox(
                        axisalignedbb.minX,
                        axisalignedbb.minY,
                        axisalignedbb.minZ,
                        axisalignedbb.maxX,
                        axisalignedbb.maxY,
                        axisalignedbb.maxZ,
                        colour.hashCode()
                );

                GlStateManager.popMatrix();
            }

            RenderUtil.endGL3D();

        }
    }

    @EventSubscribe
    public void onRenderLivingLabel(RenderLivingLabelEvent event) {
        if (this.cancelNameRender) {
            event.cancel();
        }
    }

    @EventSubscribe
    public void onOutlineActive(OutlineActiveEvent event) {
        if (this.mode.getValue() == RenderMode.GLOWING)
            if (this.getColour(event.getEntity()) != null)
                event.cancel();
    }

    @EventSubscribe
    public void onOutlineColour(TeamColourEvent event) {
        if (this.mode.getValue() == RenderMode.GLOWING) {
            final Colour colour = this.getColour(event.getEntity());
            if (colour != null) event.setColour(colour.hashCode());
        }
    }

    private Colour getColour(Entity e) {
        if (this.notSeen.getValue() && mc.player.canEntityBeSeen(e))
            return null;
        if (e instanceof EntityPlayer && e != mc.player) {
            if (Committee.playerManager.isFriend(e.getName()) && this.friends.getValue())
                return this.friendColour.getValue();
            else if (Committee.playerManager.isEnemy(e.getName()) && this.enemy.getValue())
                return this.enemyColour.getValue();
            else if (this.players.getValue()) return this.playerColour.getValue();
        } else if (((e instanceof EntityAnimal || e instanceof EntityWaterMob) && this.animals.getValue()) ||
                (e instanceof EntityMob && this.mobs.getValue()) ||
                ((e instanceof EntityVillager || e instanceof EntityIronGolem || e instanceof EntitySnowman) && this.npcs.getValue()) ||
                (e instanceof EntityEnderCrystal && this.crystals.getValue())
        ) return this.entityColour.getValue();
        return null;
    }

    private void renderShader(Framebuffer framebuffer, float partialTicks, Set<Entity> entityList, Colour colour) {
        ShaderUtil.drawShaderForEntities(
                this.shaderMode.getValue() == ShaderMode.OUTLINE ? this.outlineShader : this.spaceShader,
                framebuffer,
                partialTicks,
                entityList,
                colour,
                outlineRadius.getValue()
        );
    }

    private void getAndCreateFrameBuffers() {
        if (this.friendFramebuffer != null) this.friendFramebuffer.deleteFramebuffer();
        if (this.enemyFramebuffer != null) this.enemyFramebuffer.deleteFramebuffer();
        if (this.playerFramebuffer != null) this.playerFramebuffer.deleteFramebuffer();
        if (this.entityFramebuffer != null) this.entityFramebuffer.deleteFramebuffer();

        this.friendFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        this.enemyFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        this.playerFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        this.entityFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    }

    private enum ShaderMode {
        OUTLINE, SPACE
    }

    private enum RenderMode {
        SHADER, GLOWING, BOX, IMAGE
    }

    private enum ImageMode {
        TITTY, MISAKA
    }

}
