package net.flintloader.loader.mixin.colors;

import net.flintloader.loader.api.event.client.ColorRegistrationEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author HypherionSA
 * Mixin to accommodate Item Color Registration across multiple Modloaders
 */
@Mixin(ItemColors.class)
public class ItemColorsMixin {

    /**
     * Inject into Vanilla code to fire off our event
     * @param cir
     */
    @Inject(method = "createDefault", at = @At("RETURN"))
    private static void injectItemColors(BlockColors $$0, CallbackInfoReturnable<ItemColors> cir) {
        ColorRegistrationEvent.Items itemColorEvent = new ColorRegistrationEvent.Items(cir.getReturnValue());
        FlintEventBus.INSTANCE.postEvent(itemColorEvent);
    }

}
