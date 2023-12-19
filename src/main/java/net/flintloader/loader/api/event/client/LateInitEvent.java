package net.flintloader.loader.api.event.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

@RequiredArgsConstructor
@Getter
public class LateInitEvent extends FlintEvent {

    private final Minecraft minecraft;
    private final Options options;

    @Override
    public boolean canCancel() {
        return false;
    }
}
