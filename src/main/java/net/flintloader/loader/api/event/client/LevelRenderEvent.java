package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.renderer.LevelRenderer;

@Getter
@RequiredArgsConstructor
public class LevelRenderEvent extends FlintEvent {

    private final LevelRenderer context;

    public static class Start extends LevelRenderEvent {

        public Start(LevelRenderer level) {
            super(level);
        }

    }

    public static class End extends LevelRenderEvent {

        public End(LevelRenderer level) {
            super(level);
        }

    }

}
