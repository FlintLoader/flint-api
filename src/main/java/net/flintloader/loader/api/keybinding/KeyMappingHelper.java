/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.flintloader.loader.registry.KeyBindingRegistry;
import net.flintloader.loader.mixin.accessor.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

import java.util.Objects;

public final class KeyMappingHelper {

    public static KeyMapping registerKeyMapping(KeyMapping mapping) {
        Objects.requireNonNull(mapping, "key binding cannot be null");
        return KeyBindingRegistry.registerKeyBinding(mapping);
    }

    public static InputConstants.Key getBoundKeyOf(KeyMapping keyBinding) {
        return ((KeyMappingAccessor) keyBinding).flint_getBoundKey();
    }

}
