package net.flintloader.loader.api.screens;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.Objects;

public final class Screens {

    public static List<AbstractWidget> getButtons(Screen screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        return ScreenExtensions.getExtensions(screen).flint_getButtons();
    }

}
