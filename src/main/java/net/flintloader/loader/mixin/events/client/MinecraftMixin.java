package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.api.event.client.ScreenEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

    @Inject(method = "setScreen", at = @At(value = "TAIL"))
    private void injectScreenOpeningEvent(Screen screen, CallbackInfo ci) {
        Screen old = this.screen;
        if (screen != null) {
            ScreenEvent.Opening opening = new ScreenEvent.Opening(old, screen);
            FlintEventBus.INSTANCE.postEvent(opening);
        }
    }

}
