package net.flintloader.loader.mixin.events.client;

import net.flintloader.loader.api.event.client.SinglePlayerEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void injectSinglePlayerJoinEvent(Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            SinglePlayerEvent.PlayerLogin playerLogin = new SinglePlayerEvent.PlayerLogin(player);
            FlintEventBus.INSTANCE.postEvent(playerLogin);
        }
    }

}
