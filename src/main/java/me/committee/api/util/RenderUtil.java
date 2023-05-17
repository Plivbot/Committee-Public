package me.committee.api.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {


    /**
     * Gets the text with all the characters cut off that are outside the box unless the mouse is hovering on the text
     *
     * (THIS DOES NOT DRAW THE TEXT)
     *
     * @param name
     * @param mouseX
     * @param mouseY
     * @param x
     * @param y
     * @param width
     * @param height
     * @return the text with additional characters cut off
     */
    public static String getTextWithCutoff(String name, int mouseX, int mouseY, float x, float y, float width, float height) {
        final boolean hover = RenderUtil.isInSquare(mouseX, mouseY, x, y, width, height);
        float textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(name);

        String textToRender = name;
        int i = 0;

        while (textWidth >= width + i && textToRender.length() > 0 && !hover) {
            i--;
            textToRender = textToRender.substring(0, textToRender.length() - 1);
            textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(textToRender);
        }

        return textToRender;
    }

    public static boolean isInSquare(int x, int y, float sqx, float sqy, float sqWidth, float sqHeight) {
        return x > sqx && x < sqx + sqWidth && y > sqy && y < sqy + sqHeight;
    }

    public static void glScissorBox(float x, float y, float width, float height, ScaledResolution scaledResolution) {
        final int scaleFactor = scaledResolution.getScaleFactor();
        glScissor(
                (int) x * scaleFactor,
                (int) (scaledResolution.getScaledHeight() - (y + height)) * scaleFactor,
                (int) width * scaleFactor,
                (int) height * scaleFactor
        );
    }

    public static void drawRect(float x, float y, float width, float height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.color(red, green, blue, alpha);
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        {
            bufferbuilder.pos(x, y + height, 0.0D).endVertex();
            bufferbuilder.pos(x + width, y + height, 0.0D).endVertex();
            bufferbuilder.pos(x + width, y, 0.0D).endVertex();
            bufferbuilder.pos(x, y, 0.0D).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedRectangle(float x, float y, float width, float height, float borderWidth, int borderColour, int colour) {
        RenderUtil.drawRect(x + borderWidth, y, width - borderWidth, borderWidth, borderColour);
        RenderUtil.drawRect(x, y, borderWidth, height, borderColour);
        RenderUtil.drawRect(x + borderWidth, y + height - borderWidth, width - borderWidth, borderWidth, borderColour);
        RenderUtil.drawRect(x + width - borderWidth, y, borderWidth, height, borderColour);

        RenderUtil.drawRect(x + borderWidth, y + borderWidth, width - (borderWidth * 2), height - (borderWidth * 2), colour);
    }

    public static void drawRect(float x, float y, float width, float height, float red, float green, float blue) {
        drawRect(x, y, width, height, red, green, blue, 1);
    }

    public static void drawRect(float x, float y, float width, float height, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawRect(x, y, width, height, r, g, b, a);
    }

    public static void drawArc(float cX, float cY, float radius, int startAngle, int angleSize, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.color(red, green, blue, alpha);
        bufferbuilder.begin(GL_POLYGON, DefaultVertexFormats.POSITION);

        bufferbuilder.pos(
                cX,
                cY,
                0
        ).endVertex();

        for (int angle = startAngle; angle <= startAngle + angleSize; angle++) {
            bufferbuilder.pos(
                    cX + Math.sin(angle * Math.PI / 180.0) * radius,
                    cY + Math.cos(angle * Math.PI / 180.0) * radius,
                    0
            ).endVertex();
        }


        bufferbuilder.pos(
                cX,
                cY,
                0
        ).endVertex();

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawArc(float cX, float cY, float radius, int startAngle, int angleSize, float red, float green, float blue) {
        drawArc(cX, cY, radius, startAngle, angleSize, red, green, blue, 1);
    }

    public static void drawArc(float cX, float cY, float radius, int startAngle, int angleSize, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawArc(cX, cY, radius, startAngle, angleSize, r, g, b, a);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, float red, float green, float blue, float alpha) {
        final float diameter = 2 * radius;
        drawArc(x + radius, y + radius, radius, 180, 90, red, green, blue, alpha);
        drawArc(x + width - radius, y + radius, radius, 90, 90, red, green, blue, alpha);
        drawArc(x + width - radius, y + height - radius, radius, 0, 90, red, green, blue, alpha);
        drawArc(x + radius, y + height - radius, radius, 270, 90, red, green, blue, alpha);

        drawRect(x + radius, y, width - diameter, height, red, green, blue, alpha);
        drawRect(x, y + radius, radius, height - diameter, red, green, blue, alpha);
        drawRect(x + width - radius, y + radius, radius, height - diameter, red, green, blue, alpha);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, float red, float green, float blue) {
        drawRoundedRect(x, y, width, height, radius, red, green, blue, 1);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawRoundedRect(x, y, width, height, radius, r, g, b, a);
    }

    public static void drawTopHalfRoundedRect(float x, float y, float width, float height, float radius, float red, float green, float blue, float alpha) {
        final float diameter = 2 * radius;
        drawArc(x + radius, y + radius, radius, 180, 90, red, green, blue, alpha);
        drawArc(x + width - radius, y + radius, radius, 90, 90, red, green, blue, alpha);

        drawRect(x + radius, y, width - diameter, radius, red, green, blue, alpha);
        drawRect(x, y + radius, width, height - radius, red, green, blue, alpha);
    }

    public static void drawTopHalfRoundedRect(float x, float y, float width, float height, float radius, float red, float green, float blue) {
        drawTopHalfRoundedRect(x, y, width, height, radius, red, green, blue, 1);
    }

    public static void drawTopHalfRoundedRect(float x, float y, float width, float height, float radius, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawTopHalfRoundedRect(x, y, width, height, radius, r, g, b, a);
    }


    public static void drawBottomHalfRoundedRect(float x, float y, float width, float height, float radius, float red, float green, float blue, float alpha) {
        final float diameter = 2 * radius;
        drawArc(x + width - radius, y + height - radius, radius, 0, 90, red, green, blue, alpha);
        drawArc(x + radius, y + height - radius, radius, 270, 90, red, green, blue, alpha);

        drawRect(x, y, width, height - radius, red, green, blue, alpha);
        drawRect(x + radius, y + height - radius, width - diameter, radius, red, green, blue, alpha);
    }

    public static void drawBottomHalfRoundedRect(float x, float y, float width, float height, float radius, float red, float green, float blue) {
        drawBottomHalfRoundedRect(x, y, width, height, radius, red, green, blue, 1);
    }

    public static void drawBottomHalfRoundedRect(float x, float y, float width, float height, float radius, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawBottomHalfRoundedRect(x, y, width, height, radius, r, g, b, a);
    }


    public static void drawLine(double x, double y, double z, double x1, double y1, double z1, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(r, g, b, a);
        buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        buffer.pos(x, y, z).endVertex();
        buffer.pos(x1, y1, z1).endVertex();

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawLine(double x, double y, double z, double x1, double y1, double z1, int colour) {
        final float a = (float) (colour >> 24 & 255) / 255.0F;
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;

        drawLine(x, y, z, x1, y1, z1, r, g, b, a);
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(minX, minY, minZ).color(r, g, b, 0.0F).endVertex();
        buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, maxZ).color(r, g, b, 0.0F).endVertex();
        buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(r, g, b, 0.0F).endVertex();
        buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, minZ).color(r, g, b, 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, minZ).color(r, g, b, 0.0F).endVertex();

        tessellator.draw();
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawBoundingBox(minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;


        drawBoundingBox(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, r, g, b, a);
    }

    public static void renderBlock(BlockPos blockPos, int colour) {
        GlStateManager.pushMatrix();
        RenderUtil.startGL3D();

        final Vec3d renderTranslation = MathUtil.getRenderTranslation(new Vec3d(blockPos));

        GlStateManager.translate(renderTranslation.x, renderTranslation.y, renderTranslation.z);
        RenderUtil.drawBoundingBox(Block.FULL_BLOCK_AABB, colour);

        RenderUtil.endGL3D();
        GlStateManager.popMatrix();
    }

    public static void drawFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        {
            buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();

            buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();

            buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();

            buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();

            buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();

            buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
            buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        }

        tessellator.draw();
    }

    public static void drawFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;

        drawFilledBox(minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
    }


    public static void startGL3D() {
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
    }

    public static void endGL3D() {
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }

    public static void roundedSquare(float x, float y, float width, float height, float radius, int colour) {
        final float r = (float) (colour >> 16 & 255) / 255.0F;
        final float g = (float) (colour >> 8 & 255) / 255.0F;
        final float b = (float) (colour & 255) / 255.0F;
        final float a = (float) (colour >> 24 & 255) / 255.0F;


        roundSquare(x, y, width, height, radius, r, g, b, a);
    }

    public static void roundSquare(float x, float y, float width, float height, float radius, float red, float green, float blue, float alpha) {
        final float diameter = 2 * radius;
        circle(x + radius, y + radius, radius, 180, 90, red, green, blue, alpha);
        circle(x + width - radius, y + radius, radius, 90, 90, red, green, blue, alpha);
        circle(x + width - radius, y + height - radius, radius, 0, 90, red, green, blue, alpha);
        circle(x + radius, y + height - radius, radius, 270, 90, red, green, blue, alpha);

        square(x + radius, y, width - diameter, height, red, green, blue, alpha);
        square(x, y + radius, radius, height - diameter, red, green, blue, alpha);
        square(x + width - radius, y + radius, radius, height - diameter, red, green, blue, alpha);
    }

    public static void square(float x, float y, float width, float height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        {
            builder.pos(x, y + height, 0.0D).endVertex();
            builder.pos(x + width, y + height, 0.0D).endVertex();
            builder.pos(x + width, y, 0.0D).endVertex();
            builder.pos(x, y, 0.0D).endVertex();
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void circle(float cX, float cY, float radius, int startAngle, int angleSize, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.color(red, green, blue, alpha);
        bufferbuilder.begin(GL_POLYGON, DefaultVertexFormats.POSITION);

        bufferbuilder.pos(
                cX,
                cY,
                0
        ).endVertex();

        for (int angle = startAngle; angle <= startAngle + angleSize; angle++) {
            bufferbuilder.pos(
                    cX + Math.sin(angle * Math.PI / 180.0) * radius,
                    cY + Math.cos(angle * Math.PI / 180.0) * radius,
                    0
            ).endVertex();
        }


        bufferbuilder.pos(
                cX,
                cY,
                0
        ).endVertex();

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

}
