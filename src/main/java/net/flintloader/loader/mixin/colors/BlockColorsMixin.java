package net.flintloader.loader.mixin.colors;

import net.flintloader.loader.api.event.client.ColorRegistrationEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author HypherionSA
 * Mixin to accommodate Block Color Registration across multiple Modloaders
 */
@Mixin(BlockColors.class)
public class BlockColorsMixin {

    /**
     * Inject into Vanilla code to fire off our event
     */
    @Inject(method = "createDefault", at = @At("RETURN"))
    private static void injectBlockColors(CallbackInfoReturnable<BlockColors> cir) {
        ColorRegistrationEvent.Blocks blockEvent = new ColorRegistrationEvent.Blocks(cir.getReturnValue());
        FlintEventBus.INSTANCE.postEvent(blockEvent);
    }

}
