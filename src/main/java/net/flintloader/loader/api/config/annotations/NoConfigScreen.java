/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.config.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author HypherionSA
 * Allows Modules to disable Automatic Config Screens
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoConfigScreen {
}
