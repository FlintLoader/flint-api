/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events;

import net.flintloader.loader.api.event.common.LivingDeathEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void injectPlayerDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        LivingDeathEvent event = new LivingDeathEvent(damageSource, ((Player)(Object) this));
        FlintEventBus.INSTANCE.postEvent(event);
        if (event.wasCancelled())
            ci.cancel();
    }

}
