package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.api.event.client.ScreenEvents;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("TAIL"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V")))
    private void injectHudRenderEvent(GuiGraphics guiGraphics, float tickDelta, CallbackInfo ci) {
        FlintEventBus.INSTANCE.postEvent(new ScreenEvents.RenderHUDEvent(guiGraphics, tickDelta));
    }

}
