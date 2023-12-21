package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.FlintLifecycledResourceManager;
import net.flintloader.loader.core.resources.ResourceManagerHelperImpl;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SimpleReloadInstance.class)
public class SimpleReloadInstanceMixin {

    @Unique
    private static final ThreadLocal<PackType> flint_resourceType = new ThreadLocal<>();

    @Inject(method = "create", at = @At("HEAD"))
    private static void onCreate(ResourceManager resourceManager, List<PreparableReloadListener> list, Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, boolean bl, CallbackInfoReturnable<ReloadInstance> cir) {
        if (resourceManager instanceof FlintLifecycledResourceManager flrm) {
            flint_resourceType.set(flrm.flint_getPackType());
        }
    }

    @ModifyArg(method = "create", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;of(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/server/packs/resources/SimpleReloadInstance;"))
    private static List<PreparableReloadListener> sortSimple(List<PreparableReloadListener> reloaders) {
        List<PreparableReloadListener> sorted = ResourceManagerHelperImpl.sort(flint_resourceType.get(), reloaders);
        flint_resourceType.set(null);
        return sorted;
    }

    @Redirect(method = "create", at = @At(value = "NEW", target = "(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/server/packs/resources/ProfiledReloadInstance;"))
    private static ProfiledReloadInstance sortProfiled(ResourceManager resourceManager, List<PreparableReloadListener> reloaders, Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture) {
        List<PreparableReloadListener> sorted = ResourceManagerHelperImpl.sort(flint_resourceType.get(), reloaders);
        flint_resourceType.set(null);
        return new ProfiledReloadInstance(resourceManager, sorted, executor, executor2, completableFuture);
    }

}
