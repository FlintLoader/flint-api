/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.core.config;

import me.hypherionmc.moonconfig.core.file.FileWatcher;
import net.flintloader.loader.FlintConstants;
import org.jetbrains.annotations.ApiStatus;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author HypherionSA
 * Controls Config File Reloads and Events
 */
public final class ConfigController implements Serializable {

    /**
     * Cache of registered configs
     */
    private static final HashMap<Object, FileWatcher> monitoredConfigs = new HashMap<>();

    /**
     * INTERNAL METHOD - Register and watch the config
     *
     * @param config - The config class to register and watch
     */
    @ApiStatus.Internal
    public static void register_config(ModuleConfig config) {
        if (monitoredConfigs.containsKey(config)) {
            FlintConstants.LOG.error("Failed to register " + config.getConfigPath().getName() + ". Config already registered");
        } else {
            FileWatcher configWatcher = new FileWatcher();
            try {
                configWatcher.setWatch(config.getConfigPath(), () -> {
                    if (!config.isSaveCalled()) {
                        FlintConstants.LOG.info("Sending Reload Event for: " + config.getConfigPath().getName());
                        config.configReloaded();
                    }
                });
            } catch (Exception e) {
                FlintConstants.LOG.error("Failed to register " + config.getConfigPath().getName() + " for auto reloading. " + e.getMessage());
            }
            monitoredConfigs.put(config, configWatcher);
            FlintConstants.LOG.info("Registered " + config.getConfigPath().getName() + " successfully!");
        }
    }

    public static HashMap<Object, FileWatcher> getMonitoredConfigs() {
        return monitoredConfigs;
    }
}
