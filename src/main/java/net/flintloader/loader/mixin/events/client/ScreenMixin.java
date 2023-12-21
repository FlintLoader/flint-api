package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.api.screens.ScreenExtensions;
import net.flintloader.loader.api.screens.widgets.ButtonList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenExtensions {

    @Shadow @Final private List<Renderable> renderables;
    @Shadow @Final private List<NarratableEntry> narratables;
    @Shadow @Final private List<GuiEventListener> children;
    @Unique
    private ButtonList flintButtons;

    @Override
    public List<AbstractWidget> flint_getButtons() {
        if (flintButtons == null) {
            this.flintButtons = new ButtonList(this.renderables, this.narratables, this.children);
        }
        return flintButtons;
    }
}
