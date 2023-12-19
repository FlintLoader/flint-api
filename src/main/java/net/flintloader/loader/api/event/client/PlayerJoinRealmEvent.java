package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import com.mojang.realmsclient.dto.RealmsServer;

@RequiredArgsConstructor
@Getter
public class PlayerJoinRealmEvent extends FlintEvent {

    private final RealmsServer server;

    @Override
    public boolean canCancel() {
        return false;
    }
}