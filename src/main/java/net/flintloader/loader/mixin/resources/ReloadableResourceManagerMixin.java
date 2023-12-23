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

import com.mojang.logging.LogUtils;
import net.flintloader.loader.core.resources.GroupedResourcePack;
import net.minecraft.server.packs.PackResources;
import net.minecraft.util.Unit;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Mixin(net.minecraft.server.packs.resources.ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {

    @Redirect(method = "createReload", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V"), remap = false)
    private void getResourcePackNames(Logger instance, String s, Object o, Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, List<PackResources> list) {
        instance.info("Reloading ResourceManager: {}", LogUtils.defer(() -> list.stream().map(pack -> {
            if (pack instanceof GroupedResourcePack gp) {
                return gp.getFullName();
            } else {
                return pack.packId();
            }
        }).collect(Collectors.joining(", "))));
    }

}
