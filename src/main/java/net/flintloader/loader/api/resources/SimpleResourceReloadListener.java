package net.flintloader.loader.api.resources;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface SimpleResourceReloadListener<T> extends BrandedResourceReloadListener {

    @Override
    default @NotNull CompletableFuture<Void> reload(PreparationBarrier helper, ResourceManager manager, ProfilerFiller loadProfiler, ProfilerFiller applyProfiler, Executor loadExecutor, Executor applyExecutor) {
        return load(manager, loadProfiler, loadExecutor).thenCompose(helper::wait).thenCompose(
                (o) -> apply(o, manager, applyProfiler, applyExecutor)
        );
    }

    CompletableFuture<T> load(ResourceManager manager, ProfilerFiller profiler, Executor executor);

    CompletableFuture<Void> apply(T data, ResourceManager manager, ProfilerFiller profiler, Executor executor);
}
