package net.flintloader.loader.mixin;

import net.flintloader.loader.api.event.client.LateInitEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.tutorial.Tutorial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tutorial.class)
public class TutorialMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectEarlyInitEvent(Minecraft minecraft, Options options, CallbackInfo ci) {
        LateInitEvent event = new LateInitEvent(minecraft, options);
        FlintEventBus.INSTANCE.postEvent(event);
    }

}
