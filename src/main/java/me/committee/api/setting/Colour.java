package me.committee.api.setting;

import org.lwjgl.opengl.GL11;

public class Colour {

    private int red;
    private int green;
    private int blue;
    private int alpha;

    public Colour(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Colour(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 255;
    }

    public Colour(long colour) {
        this.red = (int) ((float) (colour >> 16 & 255) / 255.0F);
        this.green = (int) ((float) (colour >> 8 & 255) / 255.0F);
        this.blue = (int) ((float) (colour & 255) / 255.0F);
        this.alpha = (int) ((float) (colour >> 24 & 255) / 255.0F);
    }

    public Colour(long colour, boolean defaultAlphaToMax) {
        this.red = (int) ((float) (colour >> 16 & 255) / 255.0F);
        this.green = (int) ((float) (colour >> 8 & 255) / 255.0F);
        this.blue = (int) ((float) (colour & 255) / 255.0F);
        final int newAlpha = (int) ((float) (colour >> 24 & 255) / 255.0F);

        if (defaultAlphaToMax && newAlpha == 0) {
            this.alpha = 1;
        } else {
            this.alpha = newAlpha;
        }
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public float getRedF() {
        return (float) this.red / 255;
    }

    public float getGreenF() {
        return (float) this.green / 255;
    }

    public float getBlueF() {
        return (float) this.blue / 255;
    }

    public float getAlphaF() {
        return (float) this.alpha / 255;
    }

    public void setAll(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void setAll(Colour colour) {
        this.red = colour.getRed();
        this.green = colour.getGreen();
        this.blue = colour.getBlue();
        this.alpha = colour.getAlpha();
    }

    public void multiply(double value) {
        this.red *= value;
        this.green *= value;
        this.blue *= value;
        this.alpha *= value;
    }

    @Override
    public int hashCode() {
        return (this.alpha & 255) << 24 | (this.red & 255) << 16 | (this.green & 255) << 8 | (this.blue & 255);
    }

    public int hashCodeNoAlpha() {
        return (red << 16) | (green << 8) | blue;
    }

    @Override
    public String toString() {
        return "Colour{" +
                "red=" + this.red +
                ", green=" + this.green +
                ", blue=" + this.blue +
                ", alpha=" + this.alpha +
                '}';
    }

    /**
     * Sets the gl colours to the ones stored in this setting
     */
    public void setGL() {
        GL11.glColor4f(getRedF(), getBlueF(), getGreenF(), getAlpha());
    }

    public void setGLNoA() {
        GL11.glColor3f(getRedF(), getBlueF(), getGreenF());
    }
}
