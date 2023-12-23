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

package net.flintloader.loader.core.resources;

import net.flintloader.loader.api.FlintModuleContainer;
import net.flintloader.loader.modules.FlintModuleMetadata;
import net.flintloader.loader.modules.ModuleList;
import net.minecraft.locale.Language;
import net.minecraft.server.packs.PackType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ServerLanguageUtil {

    private static final String ASSETS_PREFIX = PackType.CLIENT_RESOURCES.getDirectory() + "/";

    private ServerLanguageUtil() {}

    public static Collection<Path> getModuleLanguageFiles() {
        Set<Path> paths = new LinkedHashSet<>();

        for (FlintModuleContainer container : ModuleList.getInstance().allModules()) {
            FlintModuleMetadata metadata = container.getMetadata();
            if (metadata.isBuiltIn()) continue;

            final Map<PackType, Set<String>> map = ModuleNioResoucePack.readNamespaces(container.getRootPaths(), metadata.getId());

            for (String ns : map.get(PackType.CLIENT_RESOURCES)) {
                container.findPath(ASSETS_PREFIX + ns + "/lang/" + Language.DEFAULT + ".json")
                        .filter(Files::isRegularFile)
                        .ifPresent(paths::add);
            }
        }

        return Collections.unmodifiableSet(paths);
    }

}
