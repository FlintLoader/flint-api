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

package net.flintloader.loader.registry.resources;

import net.flintloader.loader.api.resources.ModuleResourcePack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.CompositePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModuleResourcePackCreator implements RepositorySource {

    public static final PackSource RESOURCE_PACK_SOURCE = new PackSource() {
        @Override
        public Component decorate(Component component) {
            return Component.translatable("pack.nameAndSource", component, Component.translatable("pack.source.flintmodule"));
        }

        @Override
        public boolean shouldAddAutomatically() {
            return true;
        }
    };

    public static final ModuleResourcePackCreator CLIENT_RESOURCE_PACK_PROVIDER = new ModuleResourcePackCreator(PackType.CLIENT_RESOURCES);
    private final PackType type;

    public ModuleResourcePackCreator(PackType type) {
        this.type = type;
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        List<ModuleResourcePack> packs = new ArrayList<>();
        ModuleResourcePackUtil.appendModuleResourcePacks(packs, type, null);

        if (!packs.isEmpty()) {
            MutableComponent title = Component.translatable("pack.name.flintModule");

            Pack pack = Pack.readMetaAndCreate("flint", title, true, new Pack.ResourcesSupplier() {
                @Override
                public @NotNull PackResources openPrimary(String string) {
                    return new FlintModuleResourcePack(type, packs);
                }

                @Override
                public @NotNull PackResources openFull(String string, Pack.Info info) {
                    final PackResources basePack = openPrimary(string);
                    final List<String> overlays = info.overlays();

                    if (overlays.isEmpty())
                        return basePack;

                    final List<PackResources> overlayPacks = new ArrayList<>(overlays.size());

                    for (String overlay : overlays) {
                        List<ModuleResourcePack> innerPacks = new ArrayList<>();
                        ModuleResourcePackUtil.appendModuleResourcePacks(innerPacks, type, overlay);

                        overlayPacks.add(new FlintModuleResourcePack(type, innerPacks));
                    }

                    return new CompositePackResources(basePack, overlayPacks);
                }
            }, type, Pack.Position.TOP, RESOURCE_PACK_SOURCE);

            if (pack != null) {
                consumer.accept(pack);
            }
        }

        ResourceManagerHelperImpl.registerBuiltinResourcePacks(this.type, consumer);
    }
}
