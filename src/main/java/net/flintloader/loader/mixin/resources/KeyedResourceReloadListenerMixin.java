package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.api.resources.BrandedResourceReloadListener;
import net.flintloader.loader.api.resources.ResourceReloadListenerKeys;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

@Mixin({
        SoundManager.class, ModelManager.class, LanguageManager.class, TextureManager.class,
        LevelRenderer.class, BlockRenderDispatcher.class, ItemRenderer.class
})
public abstract class KeyedResourceReloadListenerMixin implements BrandedResourceReloadListener {

    private ResourceLocation flint$id;
    private Collection<ResourceLocation> flint$dependencies;

    @Override
    public ResourceLocation getFlintId() {
        if (this.flint$id == null) {
            Object self = this;

            if (self instanceof SoundManager) {
                this.flint$id = ResourceReloadListenerKeys.SOUNDS;
            } else if (self instanceof ModelManager) {
                this.flint$id = ResourceReloadListenerKeys.MODELS;
            } else if (self instanceof LanguageManager) {
                this.flint$id = ResourceReloadListenerKeys.LANGUAGES;
            } else if (self instanceof TextureManager) {
                this.flint$id = ResourceReloadListenerKeys.TEXTURES;
            } else {
                this.flint$id = new ResourceLocation("minecraft", "private/" + self.getClass().getSimpleName().toLowerCase(Locale.ROOT));
            }
        }

        return this.flint$id;
    }

    @Override
    @SuppressWarnings({"ConstantConditions"})
    public Collection<ResourceLocation> getFlintDependencies() {
        if (this.flint$dependencies == null) {
            Object self = this;

            if (self instanceof ModelManager || self instanceof LevelRenderer) {
                this.flint$dependencies = Collections.singletonList(ResourceReloadListenerKeys.TEXTURES);
            } else if (self instanceof ItemRenderer || self instanceof BlockRenderDispatcher) {
                this.flint$dependencies = Collections.singletonList(ResourceReloadListenerKeys.MODELS);
            } else {
                this.flint$dependencies = Collections.emptyList();
            }
        }

        return this.flint$dependencies;
    }
}
