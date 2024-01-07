/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.client.gui.config.widgets;

import net.flintloader.loader.client.gui.config.FlintConfigScreen;
import net.flintloader.loader.api.config.ModuleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * @author HypherionSA
 */
public class SubConfigWidget<T> extends AbstractConfigWidget<T, Button> {

    private final Object subConfig;
    private final ModuleConfig config;
    private final Screen screen;

    public SubConfigWidget(ModuleConfig config, Screen screen, Object subConfig) {
        this.config = config;
        this.subConfig = subConfig;
        this.screen = screen;

        this.widget = addChild(Button.builder(Component.translatable("t.clc.opensubconfig"), this::openSubConfig).size(200, buttonHeight).build());
    }

    @Override
    public void render(Minecraft minecraft, Font font, int x, int y, int width, int height, GuiGraphics matrices, int mouseX, int mouseY, float delta) {
        this.text = Component.literal(subConfig.getClass().getSimpleName().toLowerCase());
        this.hideReset();
        super.render(minecraft, font, x, y, width, height, matrices, mouseX, mouseY, delta);
    }

    private void openSubConfig(Button button) {
        Minecraft.getInstance().setScreen(new FlintConfigScreen(config, screen, subConfig));
    }

}
