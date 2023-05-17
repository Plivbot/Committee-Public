package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.RenderTabMenuEvent;
import me.committee.impl.event.events.render.TabOverlayPlayerEvent;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay {

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> cir) {
        final TabOverlayPlayerEvent event = new TabOverlayPlayerEvent(
                networkPlayerInfoIn.getDisplayName() != null ?
                        networkPlayerInfoIn.getDisplayName().getUnformattedComponentText() :
                        networkPlayerInfoIn.getGameProfile().getName()
        );
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            cir.setReturnValue(event.getName());
    }


    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public List<NetworkPlayerInfo> list(List<NetworkPlayerInfo> list, int i, int a) {
        RenderTabMenuEvent event = new RenderTabMenuEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            return list.subList(i, list.size());
        } else {
            return list.subList(i, a);
        }
    }


}
