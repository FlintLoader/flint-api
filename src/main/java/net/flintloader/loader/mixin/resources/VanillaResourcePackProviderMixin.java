package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.ModuleResourcePackCreator;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BuiltInPackSource.class)
public class VanillaResourcePackProviderMixin {

    @Inject(method = "loadPacks", at = @At("RETURN"))
    private void addBuiltinResourcePacks(Consumer<Pack> consumer, CallbackInfo ci) {
        // noinspection ConstantConditions
        if ((Object) this instanceof ClientPackSource) {
            ModuleResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER.loadPacks(consumer);
        }
    }
}
