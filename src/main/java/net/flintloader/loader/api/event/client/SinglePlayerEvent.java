/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;


public class SinglePlayerEvent {

    @RequiredArgsConstructor
    @Getter
    public static class PlayerLogin extends FlintEvent {
        private final LocalPlayer player;

    }

    @RequiredArgsConstructor
    @Getter
    public static class PlayerLogout extends FlintEvent {
        @Nullable private final LocalPlayer player;
    }

    @RequiredArgsConstructor
    @Getter
    public static class PlayerRespawn extends FlintEvent {
        private final LocalPlayer oldPlayer;
        private final LocalPlayer newPlayer;
    }
}
