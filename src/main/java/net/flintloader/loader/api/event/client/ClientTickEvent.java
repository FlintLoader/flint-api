package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * @author HypherionSA
 * Client Tick Event.
 */
@RequiredArgsConstructor
@Getter
public class ClientTickEvent extends FlintEvent {

    private final ClientLevel level;

    @Override
    public boolean canCancel() {
        return false;
    }
}
