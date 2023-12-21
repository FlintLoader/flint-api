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
