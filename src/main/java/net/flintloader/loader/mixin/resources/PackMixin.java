package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.ResourcePackTracker;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pack.class)
public class PackMixin {

    @Shadow @Final private PackSource packSource;

    @Inject(method = "open", at = @At("RETURN"))
    private void onOpen(CallbackInfoReturnable<PackResources> cir) {
        ResourcePackTracker.setSource(cir.getReturnValue(), packSource);
    }

}
