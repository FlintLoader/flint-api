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

import net.flintloader.loader.api.FlintModuleContainer;
import net.flintloader.loader.registry.resources.ResourceManagerHelperImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ResourceManagerHelper {

    void registerReloadListener(BrandedResourceReloadListener listener);

    static ResourceManagerHelper get(PackType type) {
        return ResourceManagerHelperImpl.get(type);
    }

    static boolean registerBuiltinResourcePack(ResourceLocation id, FlintModuleContainer metadata, ResourcePackActivationType type) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.getPath(), metadata, type);
    }

    static boolean registerBuiltinResourcePack(ResourceLocation id, FlintModuleContainer metadata, Component displayName, ResourcePackActivationType type) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.getPath(), metadata, displayName, type);
    }

}
