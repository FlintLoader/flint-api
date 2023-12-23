/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.loader;

import net.flintloader.loader.client.keybinding.KeyBindingRegistry;
import net.flintloader.loader.core.resources.ModuleNioResoucePack;
import net.flintloader.loader.core.resources.ModuleResourcePackCreator;
import net.flintloader.punch.api.PunchLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.nbt.*;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Mixin(Options.class)
public class OptionsMixin {

    @Mutable
    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Shadow @Final
    static Logger LOGGER;

    @Shadow public List<String> resourcePacks;

    @Inject(method = "load", at = @At("HEAD"))
    private void injectKeyBindings(CallbackInfo ci) {
        keyMappings = KeyBindingRegistry.process(keyMappings);

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
        Path dataDir = PunchLoader.getInstance().getGameDir().resolve("data");

        if (Files.notExists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                LOGGER.warn("[Flint Resource Loader] Could not create data directory: " + dataDir.toAbsolutePath());
            }
        }

        Path trackerFile = dataDir.resolve("flintDefaultResourcePacks.dat");
        Set<String> trackedPacks = new HashSet<>();

        if (Files.exists(trackerFile)) {
            try {
                CompoundTag data = NbtIo.readCompressed(trackerFile, NbtAccounter.unlimitedHeap());
                ListTag values = data.getList("values", Tag.TAG_STRING);

                for (int i = 0; i < values.size(); i++) {
                    trackedPacks.add(values.getString(i));
                }
            } catch (IOException e) {
                LOGGER.warn("[Fabric Resource Loader] Could not read " + trackerFile.toAbsolutePath(), e);
            }
        }

        Set<String> removedPacks = new HashSet<>(trackedPacks);
        Set<String> resourcePacks = new LinkedHashSet<>(this.resourcePacks);

        List<Pack> profiles = new ArrayList<>();
        ModuleResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER.loadPacks(profiles::add);

        for (Pack profile : profiles) {
            // Always add "Fabric Mods" pack to enabled resource packs.
            if (profile.getPackSource() == ModuleResourcePackCreator.RESOURCE_PACK_SOURCE) {
                resourcePacks.add(profile.getId());
                continue;
            }

            try (PackResources pack = profile.open()) {
                if (pack instanceof ModuleNioResoucePack builtinPack && builtinPack.getActivationType().isEnabledByDefault()) {
                    if (trackedPacks.add(builtinPack.packId())) {
                        resourcePacks.add(profile.getId());
                    } else {
                        removedPacks.remove(builtinPack.packId());
                    }
                }
            }
        }

        try {
            ListTag values = new ListTag();

            for (String id : trackedPacks) {
                if (!removedPacks.contains(id)) {
                    values.add(StringTag.valueOf(id));
                }
            }

            CompoundTag nbt = new CompoundTag();
            nbt.put("values", values);
            NbtIo.writeCompressed(nbt, trackerFile);
        } catch (IOException e) {
            LOGGER.warn("[Flint Resource Loader] Could not write to " + trackerFile.toAbsolutePath(), e);
        }

        this.resourcePacks = new ArrayList<>(resourcePacks);
    }

}
