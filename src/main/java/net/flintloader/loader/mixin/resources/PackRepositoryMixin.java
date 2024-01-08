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

import net.flintloader.loader.registry.resources.ModuleResourcePackCreator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {

    @Mutable
    @Shadow @Final
    public Set<RepositorySource> sources;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void construct(RepositorySource[] resourcePackProviders, CallbackInfo ci) {
        sources = new LinkedHashSet<>(sources);

        // Search resource pack providers to find any server-related pack provider.
        boolean shouldAddServerProvider = false;

        for (RepositorySource provider : this.sources) {
            if (provider instanceof FolderRepositorySource pv
                    && (pv.packSource == PackSource.WORLD
                    || pv.packSource == PackSource.SERVER)) {
                shouldAddServerProvider = true;
                break;
            }
        }

        // On server, add the mod resource pack provider.
        if (shouldAddServerProvider) {
            sources.add(new ModuleResourcePackCreator(PackType.SERVER_DATA));
        }
    }

}
