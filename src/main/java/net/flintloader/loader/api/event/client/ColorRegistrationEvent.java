/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;

/**
 * @author HypherionSA
 * Event to allow Block and Item Color Registration
 */
public class ColorRegistrationEvent {

    @RequiredArgsConstructor
    @Getter
    public static class Blocks extends FlintEvent {

        private final BlockColors colors;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Items extends FlintEvent {

        private final ItemColors colors;
    }

}
