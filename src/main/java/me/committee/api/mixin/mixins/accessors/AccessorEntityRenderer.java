package me.committee.api.mixin.mixins.accessors;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface AccessorEntityRenderer {

    @Invoker(value = "orientCamera")
    void cameraOrientation(float partialTicks);

    @Invoker(value = "setupCameraTransform")
    void cameraTransform(float partialTicks, int pass);

}
