package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;

public class ClientChunkEvents {

    @RequiredArgsConstructor
    @Getter
    public static class ChunkLoad extends FlintEvent {
        private final ClientLevel level;
        private final LevelChunk chunk;
    }

    @RequiredArgsConstructor
    @Getter
    public static class ChunkUnload extends FlintEvent {
        private final ClientLevel level;
        private final LevelChunk chunk;
    }

}
