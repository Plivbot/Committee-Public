package me.committee.api.shader;

import me.committee.api.setting.Colour;


public abstract class ColourShader extends Shader {

    private Colour primaryColour = new Colour(255, 255, 255, 255);
    private Colour secondaryColour = new Colour(0, 0, 0, 50);

    public ColourShader(String fragmentPath) {
        super(fragmentPath);
    }

    public void updatePrimaryColour(Colour colour) {
        this.primaryColour = colour;
    }

    public Colour getPrimaryColour() {
        return this.primaryColour;
    }

    public void updateSecondaryColour(Colour colour) {
        this.secondaryColour = colour;
    }

    public Colour getSecondaryColour() {
        return this.secondaryColour;
    }

}
