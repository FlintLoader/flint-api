/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events;

import net.flintloader.loader.api.event.common.FlintPlayerEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void injectPlayerLoginEvent(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        FlintPlayerEvent.PlayerLoggedIn loggedIn = new FlintPlayerEvent.PlayerLoggedIn(serverPlayer);
        FlintEventBus.INSTANCE.postEvent(loggedIn);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void injectPlayerLogoutEvent(ServerPlayer player, CallbackInfo ci) {
        FlintPlayerEvent.PlayerLoggedOut loggedOut = new FlintPlayerEvent.PlayerLoggedOut(player);
        FlintEventBus.INSTANCE.postEvent(loggedOut);
    }
}
