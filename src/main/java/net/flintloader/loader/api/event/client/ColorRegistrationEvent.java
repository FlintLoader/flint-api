package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;

/**
 * @author HypherionSA
 * Event to allow Block and Item Color Registration
 */
public class ColorRegistrationEvent {

    @RequiredArgsConstructor
    @Getter
    public static class Blocks extends FlintEvent {

        private final BlockColors colors;

        @Override
        public boolean canCancel() {
            return false;
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static class Items extends FlintEvent {

        private final ItemColors colors;

        @Override
        public boolean canCancel() {
            return false;
        }
    }

}
