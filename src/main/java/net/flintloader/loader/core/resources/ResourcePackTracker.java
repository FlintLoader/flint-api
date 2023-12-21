package net.flintloader.loader.core.resources;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackSource;

import java.util.WeakHashMap;

public final class ResourcePackTracker {

    private static final WeakHashMap<PackResources, PackSource> SOURCES = new WeakHashMap<>();

    public static PackSource getSource(PackResources pack) {
        return SOURCES.getOrDefault(pack, PackSource.DEFAULT);
    }

    public static void setSource(PackResources pack, PackSource source) {
        SOURCES.put(pack, source);
    }
}
