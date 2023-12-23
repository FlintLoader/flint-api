/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.client.gui.config.widgets;

import net.flintloader.loader.client.gui.config.FlintConfigScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * @author HypherionSA
 */
public class InternalConfigButton extends AbstractButton {

    FlintConfigScreen screen;
    boolean cancel;

    public InternalConfigButton(FlintConfigScreen screen, int i, int j, int k, int l, Component component, boolean cancel) {
        super(i, j, k, l, component);
        this.screen = screen;
        this.cancel = cancel;
    }

    @Override
    protected void renderWidget(GuiGraphics arg, int i, int j, float f) {
        if (cancel) {
            setMessage(Component.translatable(screen.isEdited() ? "t.clc.cancel_discard" : "gui.cancel"));
        } else {
            boolean hasErrors = screen.hasErrors();
            active = screen.isEdited() && !hasErrors;
            setMessage(Component.translatable(hasErrors ? "t.clc.error" : "t.clc.save"));
        }
        super.renderWidget(arg, i, j, f);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, getMessage());
    }

    @Override
    public void onPress() {
        if (cancel) {
            screen.onClose();
        } else {
            screen.save();
        }
    }


}
