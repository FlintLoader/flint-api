/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.events;

import net.flintloader.loader.api.event.common.AdvancementEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.AFTER))
    private void injectAdvancementEvent(AdvancementHolder advancementHolder, String string, CallbackInfoReturnable<Boolean> cir) {
        AdvancementEvent event = new AdvancementEvent(advancementHolder.value(), this.player);

        Advancement advancement = advancementHolder.value();

        if (advancement.display().isPresent() && advancement.display().get().shouldAnnounceChat()) {
            FlintEventBus.INSTANCE.postEvent(event);
        }
    }
}
