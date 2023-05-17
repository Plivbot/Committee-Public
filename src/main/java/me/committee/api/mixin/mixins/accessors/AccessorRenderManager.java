package me.committee.api.mixin.mixins.accessors;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderManager.class)
public interface AccessorRenderManager {

    @Accessor(value = "renderPosX")
    double getRenderPosX();

    @Accessor(value = "renderPosY")
    double getRenderPosY();

    @Accessor(value = "renderPosZ")
    double getRenderPosZ();

}
