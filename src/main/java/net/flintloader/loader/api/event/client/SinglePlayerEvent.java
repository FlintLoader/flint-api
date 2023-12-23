/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.world.entity.player.Player;

@RequiredArgsConstructor
@Getter
public class SinglePlayerEvent extends FlintEvent {

    private final Player player;

    public static class PlayerLogin extends SinglePlayerEvent {

        public PlayerLogin(Player player) {
            super(player);
        }

    }
}
