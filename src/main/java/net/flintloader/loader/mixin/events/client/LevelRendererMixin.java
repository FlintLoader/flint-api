package net.flintloader.loader.mixin.events.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.flintloader.loader.api.event.client.LevelRenderEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void injectLevelRenderStartEvent(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        FlintEventBus.INSTANCE.postEvent(new LevelRenderEvent.Start(((LevelRenderer) (Object) this), poseStack, camera));
    }

    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void injectLevelRenderEndEvent(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        FlintEventBus.INSTANCE.postEvent(new LevelRenderEvent.End(((LevelRenderer) (Object) this), poseStack, camera));
    }
}
