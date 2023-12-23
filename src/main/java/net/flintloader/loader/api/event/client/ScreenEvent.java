/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.gui.screens.Screen;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScreenEvent extends FlintEvent {

    private final Screen screen;

    @Getter
    public static class Opening extends ScreenEvent {

        private final Screen currentScreen;
        @Setter private Screen newScreen;

        public Opening(Screen currentScreen, Screen newScreen) {
            super(newScreen);
            this.currentScreen = currentScreen;
            this.newScreen = newScreen;
        }

    }
}
