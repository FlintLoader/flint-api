/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader;

import net.flintloader.loader.api.FlintModuleContainer;
import net.flintloader.loader.core.entrypoints.EntryPointHolder;
import net.flintloader.loader.core.entrypoints.FlintEntryPoints;
import net.flintloader.loader.core.event.FlintEventBus;
import net.flintloader.loader.modules.FlintModuleMetadata;
import net.flintloader.loader.modules.ModuleList;
import net.flintloader.punch.api.PunchLoader;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author HypherionSA
 * Main FlintLoader class to access information about the loader, api and Minecraft
 */
public final class FlintLoader {

    private FlintLoader() {}

    /**
     * @return Get the Root Minecraft Game Directory
     */
    public static File getGameDirectory() {
        return PunchLoader.getInstance().getGameDir().toFile();
    }

    /**
     * @return Get the Config directory
     */
    public static File getConfigDirectory() {
        return PunchLoader.getInstance().getConfigDir().toFile();
    }

    /**
     * Check if the game is running in a development environment
     * @return True if in development (IDE)
     */
    public static boolean isDevelopmentEnvironment() {
        return PunchLoader.getInstance().isDevelopmentEnvironment();
    }

    /**
     * Check if a module is loaded/installed
     * @param moduleId The id of the module to check for
     * @return True if the module is loaded
     */
    public static boolean isModuleLoaded(String moduleId) {
        return ModuleList.getInstance().isModuleLoaded(moduleId);
    }

    /**
     * Get a list of all loaded modules, including builtin ones
     * @return List of Module Containers or empty list
     */
    public static List<FlintModuleContainer> getLoadedModules() {
        return getLoadedModules(false);
    }

    /**
     * Get a list of all loaded modules, optionally filtering out builtin modules
     * @param includeBuiltin Should builtin modules like Minecraft/Java be included
     * @return List of Module Containers or Empty List
     */
    public static List<FlintModuleContainer> getLoadedModules(boolean includeBuiltin) {
        if (includeBuiltin) {
            return ModuleList.getInstance().allModules();
        }

        return ModuleList.getInstance().allModules().stream().filter(m -> !m.getMetadata().isBuiltIn()).toList();
    }

    /**
     * Get a count of installed modules, excluding builtin modules
     * @return Count of installed modules
     */
    public static int getModuleCount() {
        return ModuleList.getInstance().getModuleCount();
    }

    /**
     * Wrapper for {@link ModuleList#getModuleContainer(String)} that doesn't return Null
     * @param moduleId The module to get the container of
     * @return The module container if it's found
     */
    public static Optional<FlintModuleContainer> getModuleContainer(String moduleId) {
        FlintModuleContainer container = ModuleList.getInstance().getModuleContainer(moduleId);
        return container == null ? Optional.empty() : Optional.of(container);
    }

    /**
     * Wrapper for {@link ModuleList#getModuleMeta(String)} that doesn't return Null
     * @param moduleId The module to get the metadata of
     * @return The module metadata if found
     */
    public static Optional<FlintModuleMetadata> getModuleMetadata(String moduleId) {
        FlintModuleMetadata metadata = ModuleList.getInstance().getModuleMeta(moduleId);
        return metadata == null ? Optional.empty() : Optional.of(metadata);
    }

    /**
     * Access the Flint Event Bus
     * @return A copy of the Flint Event Bus
     */
    public static FlintEventBus eventBus() {
        return FlintEventBus.INSTANCE;
    }

    /**
     * Get registered entry points for a specific class and type
     * @param key The entry point key. For example "early"
     * @param type The class type
     * @return A list of registered entry points
     */
    public static List<? extends EntryPointHolder<?>> getEntryPointContainers(String key, Class<?> type) {
        return FlintEntryPoints.getEntrypointContainers(key, type);
    }

}
