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

package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.registry.resources.GroupedResourcePack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.InputStream;
import java.util.List;

@Mixin(FallbackResourceManager.class)
public class FallbackResourceManagerMixin {

    private final ThreadLocal<List<Resource>> flint$getAllResources$resources = new ThreadLocal<>();

    @Inject(method = "getResourceStack",
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onGetAllResources(ResourceLocation id, CallbackInfoReturnable<List<Resource>> cir, ResourceLocation metadataId, List<Resource> resources) {
        this.flint$getAllResources$resources.set(resources);
    }

    @Redirect(method = "getResourceStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/PackResources;getResource(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/server/packs/resources/IoSupplier;"))
    private IoSupplier<InputStream> onResourceAdd(PackResources pack, PackType type, ResourceLocation id) {
        if (pack instanceof GroupedResourcePack pack1) {
            pack1.appendResources(type, id, this.flint$getAllResources$resources.get());

            return null;
        }

        return pack.getResource(type, id);
    }

}
