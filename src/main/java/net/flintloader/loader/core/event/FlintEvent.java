package net.flintloader.loader.core.event;

import net.flintloader.loader.core.event.exception.EventCancellationException;

public abstract class FlintEvent {

    private boolean canceled = false;

    public abstract boolean canCancel();

    public void cancelEvent() {
        try {
            if (!this.canCancel()) {
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
