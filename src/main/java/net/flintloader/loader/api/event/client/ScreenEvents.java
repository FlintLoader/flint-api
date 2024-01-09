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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

@RequiredArgsConstructor
@Getter
public class ScreenEvents extends FlintEvent {

    private final Screen screen;

    @Getter
    public static class BeforeInit extends ScreenEvents {
        private final Minecraft minecraft;
        private final int scaledWidth, scaledHeight;

        public BeforeInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight) {
            super(screen);
            this.minecraft = minecraft;
            this.scaledWidth = scaledWidth;
            this.scaledHeight = scaledHeight;
        }
    }

    @Getter
    public static class AfterInit extends ScreenEvents {

        private final Minecraft minecraft;
        private final int scaledWidth, scaledHeight;

        public AfterInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight) {
            super(screen);
            this.minecraft = minecraft;
            this.scaledWidth = scaledWidth;
            this.scaledHeight = scaledHeight;
        }

    }

    public static class Removed extends ScreenEvents {

        public Removed(Screen screen) {
            super(screen);
        }

    }

    @Getter
    public static class BeforeRender extends ScreenEvents {

        private final GuiGraphics guiGraphics;
        private final int mouseX, mouseY;
        private final float tickDelta;

        public BeforeRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
            super(screen);
            this.guiGraphics = guiGraphics;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.tickDelta = tickDelta;
        }
    }

    @Getter
    public static class AfterRender extends ScreenEvents {

        private final GuiGraphics guiGraphics;
        private final int mouseX, mouseY;
        private final float tickDelta;

        public AfterRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
            super(screen);
            this.guiGraphics = guiGraphics;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.tickDelta = tickDelta;
        }
    }

    public static class BeforeTick extends ScreenEvents {

        public BeforeTick(Screen screen) {
            super(screen);
        }

    }

    public static class AfterTick extends ScreenEvents {

        public AfterTick(Screen screen) {
            super(screen);
        }

    }

    @RequiredArgsConstructor
    @Getter
    public static class RenderHUDEvent extends FlintEvent {
        private final GuiGraphics guiGraphics;
        private final float tickDelta;
    }

}
