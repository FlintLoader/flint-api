/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * @author HypherionSA
 * Client Tick Event.
 */
@RequiredArgsConstructor
@Getter
public class ClientTickEvent extends FlintEvent {

    private final Minecraft minecraft;
    private final ClientLevel level;

    public static class TickStart extends ClientTickEvent {

        public TickStart(Minecraft minecraft, ClientLevel level) {
            super(minecraft, level);
        }

    }

    public static class TickEnd extends ClientTickEvent {

        public TickEnd(Minecraft minecraft, ClientLevel level) {
            super(minecraft, level);
        }

    }
}
