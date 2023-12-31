/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.mixin.accessor;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {

    @Accessor("CATEGORY_SORT_ORDER")
    static Map<String, Integer> flint_getCategoryMap() {
        throw new AssertionError();
    }

    @Accessor("key")
    InputConstants.Key flint_getBoundKey();

}
