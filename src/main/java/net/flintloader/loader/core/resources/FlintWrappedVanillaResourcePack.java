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

import net.flintloader.loader.api.resources.ModuleResourcePack;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

public class FlintWrappedVanillaResourcePack extends GroupedResourcePack {

    private final AbstractPackResources originalResourcePack;

    public FlintWrappedVanillaResourcePack(AbstractPackResources originalResourcePack, List<ModuleResourcePack> moduleResourcePacks) {
        super(PackType.CLIENT_RESOURCES, Stream.concat(Stream.of(originalResourcePack), moduleResourcePacks.stream()).toList());
        this.originalResourcePack = originalResourcePack;
    }


    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... strings) {
        return this.originalResourcePack.getRootResource(strings);
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
        return this.originalResourcePack.getMetadataSection(metadataSectionSerializer);
    }

    @Override
    public @NotNull String packId() {
        return this.originalResourcePack.packId();
    }
}
