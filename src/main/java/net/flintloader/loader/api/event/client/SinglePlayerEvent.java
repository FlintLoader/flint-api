package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.world.entity.player.Player;

@RequiredArgsConstructor
@Getter
public class SinglePlayerEvent extends FlintEvent {

    private final Player player;

    @Override
    public boolean canCancel() {
        return false;
    }

    public static class PlayerLogin extends SinglePlayerEvent {

        public PlayerLogin(Player player) {
            super(player);
        }

    }
}
