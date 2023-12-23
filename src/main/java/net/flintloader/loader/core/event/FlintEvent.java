/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.core.event;

import net.flintloader.loader.core.event.annot.Cancellable;
import net.flintloader.loader.core.event.exception.EventCancellationException;

public abstract class FlintEvent {

    private boolean canceled = false;

    public void cancelEvent() {
        try {
            if (!this.getClass().isAnnotationPresent(Cancellable.class)) {
                throw new EventCancellationException("Tried to cancel non-cancelable event: " + this.getClass().getName());
            }

            this.canceled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean wasCancelled() {
        return this.canceled;
    }

}
