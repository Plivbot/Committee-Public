package me.committee.impl.shaders;

import me.committee.api.shader.ColourRadiusShader;
import net.minecraft.client.Minecraft;

import static org.lwjgl.opengl.GL20.*;

public class OutlineShader extends ColourRadiusShader {

    public OutlineShader() {
        super("/assets/committee/shaders/outline.frag");
    }

    @Override
    public void setupUniforms() {
        super.setupUniforms();
        setupUniform("texture");
        setupUniform("texelSize");
        setupUniform("colour");
        setupUniform("radius");
    }

    @Override
    public void updateUniforms() {
        glUniform1i(this.getUniform("texture"), 0);
        glUniform2f(this.getUniform("texelSize"), 1.0f / Minecraft.getMinecraft().displayWidth, 1.0f / Minecraft.getMinecraft().displayHeight);
        glUniform4f(this.getUniform("colour"), this.getPrimaryColour().getRedF(), this.getPrimaryColour().getGreenF(), this.getPrimaryColour().getBlueF(), this.getPrimaryColour().getAlphaF());
        glUniform1f(this.getUniform("radius"), this.getRadius());
    }

}
