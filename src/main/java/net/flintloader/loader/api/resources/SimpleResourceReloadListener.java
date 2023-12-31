/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
