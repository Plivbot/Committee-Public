package me.committee.api.util;

import me.committee.api.mixin.mixins.accessors.AccessorEntityRenderer;
import me.committee.api.setting.Colour;
import me.committee.api.shader.ColourRadiusShader;
import me.committee.api.shader.ColourShader;
import me.committee.api.shader.Shader;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;

import java.util.List;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static void startShaderEntitySetup(float partialTicks, Set<Entity> entities) {
        ((AccessorEntityRenderer) mc.entityRenderer).cameraTransform(partialTicks, 0);

        final boolean shadows = mc.gameSettings.entityShadows;
        mc.gameSettings.entityShadows = false;

        for (Entity entity : entities) {
            mc.getRenderManager().renderEntityStatic(entity, partialTicks, true);
        }

        mc.gameSettings.entityShadows = shadows;

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mc.getFramebuffer().bindFramebuffer(true);
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
    }

    private static void endShaderEntitySetup() {
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
    }

    public static void drawShaderForEntities(Shader shader, Framebuffer framebuffer, float partialTicks, Set<Entity> entities) {
        ShaderUtil.startShaderEntitySetup(partialTicks, entities);
        ShaderUtil.drawShaderFrameBuffer(shader, framebuffer, new ScaledResolution(mc));
        ShaderUtil.endShaderEntitySetup();
    }

    public static void drawShaderForEntities(ColourShader shader, Framebuffer framebuffer, float partialTicks, Set<Entity> entities, Colour colour) {
        ShaderUtil.startShaderEntitySetup(partialTicks, entities);
        ShaderUtil.drawShaderFrameBuffer(shader, framebuffer, new ScaledResolution(mc), colour, null);
        ShaderUtil.endShaderEntitySetup();
    }

    public static void drawShaderForEntities(ColourShader shader, Framebuffer framebuffer, float partialTicks, Set<Entity> entities, Colour primaryColour, Colour secondaryColour) {
        ShaderUtil.startShaderEntitySetup(partialTicks, entities);
        ShaderUtil.drawShaderFrameBuffer(shader, framebuffer, new ScaledResolution(mc), primaryColour, secondaryColour);
        ShaderUtil.endShaderEntitySetup();
    }

    public static void drawShaderForEntities(ColourRadiusShader shader, Framebuffer framebuffer, float partialTicks, Set<Entity> entities, Colour primaryColour, Colour secondaryColour, float radius) {
        ShaderUtil.startShaderEntitySetup(partialTicks, entities);
        ShaderUtil.drawShaderFrameBuffer(shader, framebuffer, new ScaledResolution(mc), primaryColour, secondaryColour, radius);
        ShaderUtil.endShaderEntitySetup();
    }

    public static void drawShaderForEntities(ColourRadiusShader shader, Framebuffer framebuffer, float partialTicks, Set<Entity> entities, Colour colour, float radius) {
        ShaderUtil.startShaderEntitySetup(partialTicks, entities);
        ShaderUtil.drawShaderFrameBuffer(shader, framebuffer, new ScaledResolution(mc), colour, null, radius);
        ShaderUtil.endShaderEntitySetup();
    }

    public static void drawShaderFrameBuffer(Shader shader, Framebuffer framebuffer, ScaledResolution scaledResolution) {
        drawShaderAndOverlay(shader, framebuffer, scaledResolution);
    }

    public static void drawShaderFrameBuffer(ColourShader shader, Framebuffer framebuffer, ScaledResolution scaledResolution, Colour primaryColour, Colour secondaryColour) {
        if (primaryColour != null)
            shader.updatePrimaryColour(primaryColour);

        if (secondaryColour != null)
            shader.updateSecondaryColour(secondaryColour);

        drawShaderAndOverlay(shader, framebuffer, scaledResolution);
    }

    public static void drawShaderFrameBuffer(ColourRadiusShader shader, Framebuffer framebuffer, ScaledResolution scaledResolution, Colour primaryColour, Colour secondaryColour, float radius) {
        if (primaryColour != null)
            shader.updatePrimaryColour(primaryColour);

        if (secondaryColour != null)
            shader.updateSecondaryColour(secondaryColour);

        shader.setRadius(radius);

        drawShaderAndOverlay(shader, framebuffer, scaledResolution);
    }

    private static void drawShaderAndOverlay(Shader shader, Framebuffer framebuffer, ScaledResolution scaledResolution) {
        glPushMatrix();

        shader.draw();

        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();

        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 1);
            glVertex2d(0, 0);
            glTexCoord2d(0, 0);
            glVertex2d(0, scaledResolution.getScaledHeight());
            glTexCoord2d(1, 0);
            glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            glTexCoord2d(1, 1);
            glVertex2d(scaledResolution.getScaledWidth(), 0);
        }
        glEnd();
        glUseProgram(0);

        glUseProgram(0);
        glPopMatrix();
    }

}
