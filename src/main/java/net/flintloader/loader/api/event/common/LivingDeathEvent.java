package net.flintloader.loader.api.event.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.flintloader.loader.core.event.annot.Cancellable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@RequiredArgsConstructor
@Getter
@Cancellable
public class LivingDeathEvent extends FlintEvent {

    private final DamageSource damageSource;
    private final LivingEntity entity;

}
