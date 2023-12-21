package net.flintloader.loader.api.resources;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Collection;
import java.util.Collections;

public interface BrandedResourceReloadListener extends PreparableReloadListener {

    ResourceLocation getFlintId();

    default Collection<ResourceLocation> getFlintDependencies() {
        return Collections.emptyList();
    }

}
