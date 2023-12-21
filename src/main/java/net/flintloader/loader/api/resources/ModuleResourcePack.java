package net.flintloader.loader.api.resources;

import net.flintloader.loader.api.FlintModuleContainer;
import net.minecraft.server.packs.PackResources;

public interface ModuleResourcePack extends PackResources {

    FlintModuleContainer getContainer();

}
