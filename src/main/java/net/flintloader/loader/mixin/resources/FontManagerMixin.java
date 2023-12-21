package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.api.resources.BrandedResourceReloadListener;
import net.flintloader.loader.api.resources.ResourceReloadListenerKeys;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FontManager.class)
public abstract class FontManagerMixin implements BrandedResourceReloadListener {

    @Override
    public ResourceLocation getFlintId() {
        return ResourceReloadListenerKeys.FONTS;
    }
}
