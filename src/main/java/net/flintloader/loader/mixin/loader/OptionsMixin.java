package net.flintloader.loader.mixin.loader;

import net.flintloader.loader.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {

    @Mutable
    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Inject(method = "load", at = @At("HEAD"))
    private void injectKeyBindings(CallbackInfo ci) {
        keyMappings = KeyBindingRegistry.process(keyMappings);
    }

}
