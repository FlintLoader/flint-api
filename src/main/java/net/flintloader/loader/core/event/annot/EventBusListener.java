/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.core.event.annot;

import net.flintloader.loader.core.event.FlintEventPriority;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventBusListener {
    int priority() default FlintEventPriority.NORMAL;
}
