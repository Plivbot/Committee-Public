package me.committee.api.mixin.mixins.accessors;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.FloatBuffer;

@Mixin(ActiveRenderInfo.class)
public interface AccessorActiveRenderInfo {

    @Accessor("MODELVIEW")
    static FloatBuffer getMODELVIEW() {
        return null;
    }

    @Accessor("PROJECTION")
    static FloatBuffer getPROJECTION() {
        return null;
    }

}
