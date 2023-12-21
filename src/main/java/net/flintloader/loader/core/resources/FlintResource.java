package net.flintloader.loader.core.resources;

import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.LoggerFactory;

public interface FlintResource {

    default PackSource getFlintPackSource() {
        LoggerFactory.getLogger(FlintResource.class).error("Unknown Resource implementation {}, returning DEFAULT as the source", getClass().getName());
        return PackSource.DEFAULT;
    }

}
