/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.entity.player.Player;

@RequiredArgsConstructor
@Getter
public class AdvancementEvent extends FlintEvent {

    private final Advancement advancement;
    private final Player player;

}
