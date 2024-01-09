/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;

public class LevelRenderEvent {

    @Getter
    @RequiredArgsConstructor
    public static class Start extends FlintEvent {

        private final LevelRenderer context;
        private final PoseStack poseStack;
        private final Camera camera;

    }

    @Getter
    @RequiredArgsConstructor
    public static class End extends FlintEvent {

        private final LevelRenderer context;
        private final PoseStack poseStack;
        private final Camera camera;

    }

}
