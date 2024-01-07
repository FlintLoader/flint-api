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

import net.flintloader.loader.registry.resources.BuiltInModuleResourcePackSource;
import net.flintloader.loader.registry.resources.ModuleNioResoucePack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Redirect(method = "configurePackRepository", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"))
    private static boolean onCheckDisabled(List<String> list, Object o, PackRepository resourcePackManager) {
        String profileName = (String) o;
        boolean contains = list.contains(profileName);

        if (contains) {
            return true;
        }

        Pack profile = resourcePackManager.getPack(profileName);

        if (profile.getPackSource() instanceof BuiltInModuleResourcePackSource) {
            try (PackResources pack = profile.open()) {
                // Prevents automatic load for built-in data packs provided by mods.
                return pack instanceof ModuleNioResoucePack modPack && !modPack.getActivationType().isEnabledByDefault();
            }
        }

        return false;
    }

}
