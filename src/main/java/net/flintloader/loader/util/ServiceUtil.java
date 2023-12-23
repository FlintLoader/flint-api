/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.util;

import net.flintloader.loader.FlintConstants;

import java.util.ServiceLoader;

/**
 * @author HypherionSA
 * Utility class to handle SPI loading
 */
public class ServiceUtil {

    /**
     * Try to load a service
     * @param clazz The service class type to load
     * @return The loaded class
     */
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        FlintConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}
