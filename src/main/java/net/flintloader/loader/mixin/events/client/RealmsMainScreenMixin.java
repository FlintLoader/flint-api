/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events.client;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import net.flintloader.loader.api.event.client.PlayerJoinRealmEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RealmsMainScreen.class)
public class RealmsMainScreenMixin {

    @Inject(at = @At("HEAD"), method = "play(Lcom/mojang/realmsclient/dto/RealmsServer;Lnet/minecraft/client/gui/screens/Screen;Z)V")
    private static void play(RealmsServer realmsServer, Screen screen, boolean bl, CallbackInfo ci) {
        PlayerJoinRealmEvent playerJoinRealm = new PlayerJoinRealmEvent(realmsServer);
        FlintEventBus.INSTANCE.postEvent(playerJoinRealm);
    }

}
