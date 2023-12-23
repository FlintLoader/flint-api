/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.api.resources.BrandedResourceReloadListener;
import net.flintloader.loader.api.resources.ResourceReloadListenerKeys;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FontManager.class)
public abstract class FontManagerMixin implements BrandedResourceReloadListener {

    @Override
    public ResourceLocation getFlintId() {
        return ResourceReloadListenerKeys.FONTS;
    }
}
