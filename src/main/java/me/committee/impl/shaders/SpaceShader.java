package me.committee.impl.shaders;

import me.committee.api.shader.ColourRadiusShader;
import net.minecraft.client.Minecraft;

import static org.lwjgl.opengl.GL20.*;

public class SpaceShader extends ColourRadiusShader {

    public long time = 0;

    public SpaceShader() {
        super("/assets/committee/shaders/space.frag");
    }

    @Override
    public void setupUniforms() {
        super.setupUniforms();
        setupUniform("texture");
        setupUniform("texelSize");
        setupUniform("colour");
        setupUniform("radius");
        setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        glUniform1i(this.getUniform("texture"), 0);
        glUniform2f(this.getUniform("texelSize"), 1.0f / Minecraft.getMinecraft().displayWidth, 1.0f / Minecraft.getMinecraft().displayHeight);
        glUniform4f(this.getUniform("colour"), this.getPrimaryColour().getRedF(), this.getPrimaryColour().getGreenF(), this.getPrimaryColour().getBlueF(), this.getPrimaryColour().getAlphaF());
        glUniform1f(this.getUniform("radius"), this.getRadius());
        glUniform1f(this.getUniform("time"), time);
        time += 1;
    }

}
