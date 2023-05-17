package me.committee.api.util;


public class ThemedRenderUtil {

    public static void drawRectangle(float x, float y, float width, float height) {
        RenderUtil.drawRect(x, y, width, height, 0xEE282A36);
    }

    public static void drawArc(float cX, float cY, float radius, int startAngle, int angleSize) {
        RenderUtil.drawArc(cX, cY, radius, startAngle, angleSize, 0xEE282A36);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius) {
        RenderUtil.drawRoundedRect(x, y, width, height, radius, 0xEE282A36);
    }

    public static void drawBottomHalfRoundedRect(float x, float y, float width, float height, float radius) {
        RenderUtil.drawBottomHalfRoundedRect(x, y, width, height, radius, 0xEE282A36);
    }


}
