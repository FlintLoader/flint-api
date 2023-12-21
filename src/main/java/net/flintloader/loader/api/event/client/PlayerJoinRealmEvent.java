package net.flintloader.loader.api.event.client;

import com.mojang.realmsclient.dto.RealmsServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;

@RequiredArgsConstructor
@Getter
public class PlayerJoinRealmEvent extends FlintEvent {

    private final RealmsServer server;
}