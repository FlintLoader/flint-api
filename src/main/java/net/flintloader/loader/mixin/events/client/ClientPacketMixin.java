/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.api.event.client.SinglePlayerEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketMixin extends ClientCommonPacketListenerImpl {

    @Unique
    private LocalPlayer flint_api$tmpPlayer;

    protected ClientPacketMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;setServerRenderDistance(I)V", shift = At.Shift.AFTER))
    private void injectPlayerLoginEvent(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {
        FlintEventBus.INSTANCE.postEvent(new SinglePlayerEvent.PlayerLogin(minecraft.player));
    }

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void injectRespawnEventPre(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        this.flint_api$tmpPlayer = minecraft.player;
    }

    @Inject(method = "handleRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addEntity(Lnet/minecraft/world/entity/Entity;)V"))
    private void injectRespawnEvent(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        FlintEventBus.INSTANCE.postEvent(new SinglePlayerEvent.PlayerRespawn(flint_api$tmpPlayer, minecraft.player));
        this.flint_api$tmpPlayer = null;
    }
}
