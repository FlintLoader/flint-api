package net.flintloader.loader.api.resources;

import net.flintloader.loader.api.FlintModuleContainer;
import net.flintloader.loader.core.resources.ResourceManagerHelperImpl;
import net.flintloader.loader.modules.FlintModuleMetadata;
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
