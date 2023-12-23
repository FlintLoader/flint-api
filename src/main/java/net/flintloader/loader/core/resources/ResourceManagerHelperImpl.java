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

package net.flintloader.loader.core.resources;

import com.google.common.collect.Lists;
import net.flintloader.loader.api.FlintModuleContainer;
import net.flintloader.loader.api.resources.BrandedResourceReloadListener;
import net.flintloader.loader.api.resources.ResourceManagerHelper;
import net.flintloader.loader.api.resources.ResourcePackActivationType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class ResourceManagerHelperImpl implements ResourceManagerHelper {

    private static final Map<PackType, ResourceManagerHelperImpl> registryMap = new HashMap<>();
    private static final Set<Pair<Component, ModuleNioResoucePack>> builtinResourcePacks = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManagerHelperImpl.class);

    private final Set<ResourceLocation> addedListenerIds = new HashSet<>();
    private final Set<BrandedResourceReloadListener> addedListeners = new LinkedHashSet<>();

    public static ResourceManagerHelperImpl get(PackType type) {
        return registryMap.computeIfAbsent(type, (t) -> new ResourceManagerHelperImpl());
    }

    public static boolean registerBuiltinResourcePack(ResourceLocation id, String subPath, FlintModuleContainer container, Component displayName, ResourcePackActivationType activationType) {
        List<Path> paths = container.getRootPaths();
        String separator = paths.get(0).getFileSystem().getSeparator();
        subPath = subPath.replace("/", separator);
        ModuleNioResoucePack resourcePack = ModuleNioResoucePack.create(id.toString(), container, subPath, PackType.CLIENT_RESOURCES, activationType);
        ModuleNioResoucePack dataPack = ModuleNioResoucePack.create(id.toString(), container, subPath, PackType.SERVER_DATA, activationType);
        if (resourcePack == null && dataPack == null) return false;

        if (resourcePack != null) {
            builtinResourcePacks.add(Pair.of(displayName, resourcePack));
        }

        if (dataPack != null) {
            builtinResourcePacks.add(Pair.of(displayName, dataPack));
        }

        return true;
    }

    public static boolean registerBuiltinResourcePack(ResourceLocation id, String subPath, FlintModuleContainer container, ResourcePackActivationType activationType) {
        return registerBuiltinResourcePack(id, subPath, container, Component.literal(id.getNamespace() + "/" + id.getPath()), activationType);
    }

    public static void registerBuiltinResourcePacks(PackType resourceType, Consumer<Pack> consumer) {
        // Loop through each registered built-in resource packs and add them if valid.
        for (Pair<Component, ModuleNioResoucePack> entry : builtinResourcePacks) {
            ModuleNioResoucePack pack = entry.getRight();

            // Add the built-in pack only if namespaces for the specified resource type are present.
            if (!pack.getNamespaces(resourceType).isEmpty()) {
                // Make the resource pack profile for built-in pack, should never be always enabled.
                Pack profile = Pack.readMetaAndCreate(entry.getRight().packId(), entry.getLeft(), pack.getActivationType() == ResourcePackActivationType.ALWAYS_ENABLED, new Pack.ResourcesSupplier() {
                    @Override
                    public @NotNull PackResources openPrimary(String string) {
                        return entry.getRight();
                    }

                    @Override
                    public @NotNull PackResources openFull(String string, Pack.Info info) {
                        return entry.getRight();
                    }
                }, resourceType, Pack.Position.TOP, new BuiltInModuleResourcePackSource(pack.getContainer().getMetadata().getName()));
                consumer.accept(profile);
            }
        }
    }

    public static List<PreparableReloadListener> sort(PackType type, List<PreparableReloadListener> listeners) {
        if (type == null) {
            return listeners;
        }

        ResourceManagerHelperImpl instance = get(type);

        if (instance != null) {
            List<PreparableReloadListener> mutable = new ArrayList<>(listeners);
            instance.sort(mutable);
            return Collections.unmodifiableList(mutable);
        }

        return listeners;
    }

    protected void sort(List<PreparableReloadListener> listeners) {
        listeners.removeAll(addedListeners);
        List<BrandedResourceReloadListener> listenersToAdd = Lists.newArrayList(addedListeners);
        Set<ResourceLocation> resolvedIds = new HashSet<>();

        for (PreparableReloadListener listener : listeners) {
            if (listener instanceof BrandedResourceReloadListener ll) {
                resolvedIds.add(ll.getFlintId());
            }
        }

        int lastSize = -1;

        while (listeners.size() != lastSize) {
            lastSize = listeners.size();

            Iterator<BrandedResourceReloadListener> it = listenersToAdd.iterator();

            while (it.hasNext()) {
                BrandedResourceReloadListener listener = it.next();

                if (resolvedIds.containsAll(listener.getFlintDependencies())) {
                    resolvedIds.add(listener.getFlintId());
                    listeners.add(listener);
                    it.remove();
                }
            }
        }

        for (BrandedResourceReloadListener listener : listenersToAdd) {
            LOGGER.warn("Could not resolve dependencies for listener: " + listener.getFlintId() + "!");
        }
    }

    @Override
    public void registerReloadListener(BrandedResourceReloadListener listener) {
        if (!addedListenerIds.add(listener.getFlintId())) {
            LOGGER.warn("Tried to register resource reload listener " + listener.getFlintId() + " twice!");
            return;
        }

        if (!addedListeners.add(listener)) {
            throw new RuntimeException("Listener with previously unknown ID " + listener.getFlintId() + " already in listener set!");
        }
    }
}
