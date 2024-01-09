/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.FlintLoader;
import net.flintloader.loader.api.event.client.ClientTickEvent;
import net.flintloader.loader.api.event.client.ScreenEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable
    public Screen screen;

    @Shadow @Nullable public ClientLevel level;

    @Inject(method = "setScreen", at = @At(value = "TAIL"))
    private void injectScreenOpeningEvent(Screen screen, CallbackInfo ci) {
        Screen old = this.screen;
        if (screen != null) {
            ScreenEvent.Opening opening = new ScreenEvent.Opening(old, screen);
            FlintEventBus.INSTANCE.postEvent(opening);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectClientTickStartEvent(CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ClientTickEvent.TickStart(((Minecraft) (Object) this), level));
    }

    @Inject(method =  "tick", at = @At("RETURN"))
    private void injectClientTickEndEvent(CallbackInfo ci) {
        FlintLoader.eventBus().postEvent(new ClientTickEvent.TickEnd(((Minecraft) (Object) this), level));
    }
}
