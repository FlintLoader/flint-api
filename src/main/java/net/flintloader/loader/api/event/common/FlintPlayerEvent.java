package net.flintloader.loader.api.event.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.server.level.ServerPlayer;

@RequiredArgsConstructor
@Getter
public class FlintPlayerEvent extends FlintEvent {

    private final ServerPlayer player;

    @Override
    public boolean canCancel() {
        return false;
    }

    public static class PlayerLoggedIn extends FlintPlayerEvent {

        public PlayerLoggedIn(ServerPlayer player) {
            super(player);
        }

    }

    public static class PlayerLoggedOut extends FlintPlayerEvent {

        public PlayerLoggedOut(ServerPlayer player) {
            super(player);
        }

    }
}
