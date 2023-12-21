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
