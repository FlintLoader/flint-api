/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author HypherionSA
 * Used to determine if a Config section should be rendered as a separate screen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SubConfig {
}

