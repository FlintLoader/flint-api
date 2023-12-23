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

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParseException;
import net.flintloader.loader.core.resources.ServerLanguageUtil;
import net.minecraft.locale.Language;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(Language.class)
public class LanguageMixin {

    @Shadow @Final private static Logger LOGGER;

    @Shadow public static void loadFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {}

    @Redirect(method = "loadDefault", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"), remap = false)
    private static ImmutableMap<String, String> create(ImmutableMap.Builder<String, String> cir) {
        Map<String, String> map = new HashMap<>(cir.buildOrThrow());

        for (Path path : ServerLanguageUtil.getModuleLanguageFiles()) {
            loadFromPath(path, map::put);
        }

        return ImmutableMap.copyOf(map);
    }

    private static void loadFromPath(Path path, BiConsumer<String, String> entryConsumer) {
        try (InputStream stream = Files.newInputStream(path)) {
            LOGGER.debug("Loading translations from {}", path);
            loadFromJson(stream, entryConsumer);
        } catch (JsonParseException | IOException e) {
            LOGGER.error("Couldn't read strings from {}", path, e);
        }
    }

}
