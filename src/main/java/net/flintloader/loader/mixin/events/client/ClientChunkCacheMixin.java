/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.api.event.client.ClientChunkEvents;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin {

    @Shadow @Final
    ClientLevel level;

    @Inject(method = "replaceWithPacketData", at = @At("TAIL"))
    private void injectChunkLoad(int i, int j, FriendlyByteBuf friendlyByteBuf, CompoundTag compoundTag, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> cir) {
        FlintEventBus.INSTANCE.postEvent(new ClientChunkEvents.ChunkLoad(this.level, cir.getReturnValue()));
    }

    @Inject(method = "replaceWithPacketData", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/chunk/LevelChunk;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectChunkUnload(int i, int j, FriendlyByteBuf friendlyByteBuf, CompoundTag compoundTag, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> cir, int k, LevelChunk levelChunk, ChunkPos chunkPos) {
        if (levelChunk != null) {
            FlintEventBus.INSTANCE.postEvent(new ClientChunkEvents.ChunkUnload(this.level, levelChunk));
        }
    }

    @Inject(method = "drop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;replace(ILnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/chunk/LevelChunk;)Lnet/minecraft/world/level/chunk/LevelChunk;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectChunkUnload(ChunkPos chunkPos, CallbackInfo ci, int i, LevelChunk levelChunk) {
        FlintEventBus.INSTANCE.postEvent(new ClientChunkEvents.ChunkUnload(this.level, levelChunk));
    }

    @Inject(method = "updateViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;inRange(II)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectChunkUnload(int i, CallbackInfo ci, int j, int k, ClientChunkCache.Storage storage, int l, LevelChunk levelChunk, ChunkPos chunkPos) {
        if (!storage.inRange(chunkPos.x, chunkPos.z)) {
            FlintEventBus.INSTANCE.postEvent(new ClientChunkEvents.ChunkUnload(this.level, levelChunk));
        }
    }
}
