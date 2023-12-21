package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.FlintLifecycledResourceManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin implements FlintLifecycledResourceManager {

    @Unique
    private PackType flint_resourceType;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(PackType packType, List<PackResources> list, CallbackInfo ci) {
        this.flint_resourceType = packType;
    }

    @Override
    public PackType flint_getPackType() {
        return flint_resourceType;
    }
}
