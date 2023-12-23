/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.FlintLoader;
import net.flintloader.loader.api.event.client.ScreenEvents;
import net.flintloader.loader.api.screens.ScreenExtensions;
import net.flintloader.loader.api.screens.widgets.ButtonList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenExtensions {

    @Shadow @Final private List<Renderable> renderables;
    @Shadow @Final private List<NarratableEntry> narratables;
    @Shadow @Final private List<GuiEventListener> children;
    @Unique
    private ButtonList flintButtons;

    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("HEAD"))
    private void injectScreenInitEvent(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.BeforeInit(minecraft, (Screen) ((Object) this), width, height));
    }

    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("TAIL"))
    private void injectScreenAfterInitEvent(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.AfterInit(minecraft, (Screen) ((Object) this), width, height));
    }

    @Inject(method = "resize", at = @At("HEAD"))
    private void beforeResizeScreen(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.BeforeInit(minecraft, (Screen) ((Object) this), width, height));
    }

    @Inject(method = "resize", at = @At("TAIL"))
    private void afterResizeScreen(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.AfterInit(minecraft, (Screen) ((Object) this), width, height));
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void injectBeforeRenderEvent(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.BeforeRender((Screen) ((Object) this), guiGraphics, mouseX, mouseY, tickDelta));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void injectAfterRenderEvent(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.AfterRender((Screen) ((Object) this), guiGraphics, mouseX, mouseY, tickDelta));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectBeforeTickEvent(CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.BeforeTick((Screen) ((Object) this)));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectAfterTickEvent(CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.AfterTick((Screen) ((Object) this)));
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void injectRemovedEvent(CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ScreenEvents.Removed((Screen) ((Object) this)));
    }

    @Override
    public List<AbstractWidget> flint_getButtons() {
        if (flintButtons == null) {
            this.flintButtons = new ButtonList(this.renderables, this.narratables, this.children);
        }
        return flintButtons;
    }
}
