package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderLayerArmourEvent extends Event {

    private final EntityLivingBase entityLivingBase;

    public RenderLayerArmourEvent(EntityLivingBase entityLivingBase) {
        this.entityLivingBase = entityLivingBase;
    }

    public EntityLivingBase getEntityLivingBase() {
        return entityLivingBase;
    }

}
