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
import net.flintloader.loader.api.resources.ModuleResourcePack;
import net.flintloader.loader.api.resources.ResourcePackActivationType;
import net.minecraft.FileUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;

public class ModuleNioResoucePack implements PackResources, ModuleResourcePack {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleNioResoucePack.class);
    private static final Pattern RESOURCE_PACK_PATH = Pattern.compile("[a-z0-9-_.]+");
    private static final FileSystem DEFAULT_FS = FileSystems.getDefault();

    private final String id;
    private final FlintModuleContainer metadata;
    private final List<Path> basePaths;
    private final PackType type;
    private final AutoCloseable closer;
    private final ResourcePackActivationType activationType;
    private final Map<PackType, Set<String>> namespaces;

    public static ModuleNioResoucePack create(String id, FlintModuleContainer module, String subPath, PackType type, ResourcePackActivationType activationType) {
        List<Path> rootPaths = module.getRootPaths();
        List<Path> paths;

        if (subPath == null) {
            paths = rootPaths;
        } else {
            paths = new ArrayList<>(rootPaths.size());

            for (Path path : rootPaths) {
                path = path.toAbsolutePath().normalize();
                Path childPath = path.resolve(subPath.replace("/", path.getFileSystem().getSeparator())).normalize();

                if (!childPath.startsWith(path) || !exists(childPath)) {
                    continue;
                }

                paths.add(childPath);
            }
        }

        if (paths.isEmpty()) return null;

        ModuleNioResoucePack ret = new ModuleNioResoucePack(id, module, paths, type, null, activationType);

        return ret.getNamespaces(type).isEmpty() ? null : ret;
    }

    private ModuleNioResoucePack(String id, FlintModuleContainer metadata, List<Path> paths, PackType type, AutoCloseable closer, ResourcePackActivationType activationType) {
        this.id = id;
        this.metadata = metadata;
        this.basePaths = paths;
        this.type = type;
        this.closer = closer;
        this.activationType = activationType;
        this.namespaces = readNamespaces(paths, metadata.getMetadata().getId());
    }

    static Map<PackType, Set<String>> readNamespaces(List<Path> paths, String modId) {
        Map<PackType, Set<String>> ret = new EnumMap<>(PackType.class);

        for (PackType type : PackType.values()) {
            Set<String> namespaces = null;

            for (Path path : paths) {
                Path dir = path.resolve(type.getDirectory());
                if (!Files.isDirectory(dir)) continue;

                String separator = path.getFileSystem().getSeparator();

                try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
                    for (Path p : ds) {
                        if (!Files.isDirectory(p)) continue;

                        String s = p.getFileName().toString();
                        // s may contain trailing slashes, remove them
                        s = s.replace(separator, "");

                        if (!RESOURCE_PACK_PATH.matcher(s).matches()) {
                            LOGGER.warn("Flint NioResourcePack: ignored invalid namespace: {} in mod ID {}", s, modId);
                            continue;
                        }

                        if (namespaces == null) namespaces = new HashSet<>();

                        namespaces.add(s);
                    }
                } catch (IOException e) {
                    LOGGER.warn("getNamespaces in mod " + modId + " failed!", e);
                }
            }

            ret.put(type, namespaces != null ? namespaces : Collections.emptySet());
        }

        return ret;
    }

    private Path getPath(String filename) {
        if (hasAbsentNs(filename)) return null;

        for (Path basePath : basePaths) {
            Path childPath = basePath.resolve(filename.replace("/", basePath.getFileSystem().getSeparator())).toAbsolutePath().normalize();

            if (childPath.startsWith(basePath) && exists(childPath)) {
                return childPath;
            }
        }

        return null;
    }

    private static final String resPrefix = PackType.CLIENT_RESOURCES.getDirectory() + "/";
    private static final String dataPrefix = PackType.SERVER_DATA.getDirectory() + "/";

    private boolean hasAbsentNs(String filename) {
        int prefixLen;
        PackType type;

        if (filename.startsWith(resPrefix)) {
            prefixLen = resPrefix.length();
            type = PackType.CLIENT_RESOURCES;
        } else if (filename.startsWith(dataPrefix)) {
            prefixLen = dataPrefix.length();
            type = PackType.SERVER_DATA;
        } else {
            return false;
        }

        int nsEnd = filename.indexOf('/', prefixLen);
        if (nsEnd < 0) return false;

        return !namespaces.get(type).contains(filename.substring(prefixLen, nsEnd));
    }

    private IoSupplier<InputStream> openFile(String filename) {
        Path path = getPath(filename);

        if (path != null && Files.isRegularFile(path)) {
            return () -> Files.newInputStream(path);
        }

        if (ModuleResourcePackUtil.containsDefault(this.metadata, filename)) {
            return () -> ModuleResourcePackUtil.openDefault(this.metadata, this.type, filename);
        }

        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... pathSegments) {
        FileUtil.validatePath(pathSegments);

        return this.openFile(String.join("/", pathSegments));
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation id) {
        final Path path = getPath(getFilename(type, id));
        return path == null ? null : IoSupplier.create(path);
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput visitor) {
        if (!namespaces.getOrDefault(type, Collections.emptySet()).contains(namespace)) {
            return;
        }

        for (Path basePath : basePaths) {
            String separator = basePath.getFileSystem().getSeparator();
            Path nsPath = basePath.resolve(type.getDirectory()).resolve(namespace);
            Path searchPath = nsPath.resolve(path.replace("/", separator)).normalize();
            if (!exists(searchPath)) continue;

            try {
                Files.walkFileTree(searchPath, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        String filename = nsPath.relativize(file).toString().replace(separator, "/");
                        ResourceLocation identifier = ResourceLocation.tryBuild(namespace, filename);

                        if (identifier == null) {
                            LOGGER.error("Invalid path in mod resource-pack {}: {}:{}, ignoring", id, namespace, filename);
                        } else {
                            visitor.accept(identifier, IoSupplier.create(file));
                        }

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                LOGGER.warn("findResources at " + path + " in namespace " + namespace + ", module " + metadata.getMetadata().getId() + " failed!", e);
            }
        }
    }

    @Override
    public @NotNull Set<String> getNamespaces(PackType packType) {
        return namespaces.getOrDefault(type, Collections.emptySet());
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) throws IOException {
        try (InputStream is = openFile("pack.mcmeta").get()) {
            return AbstractPackResources.getMetadataFromStream(metaReader, is);
        }
    }

    @Override
    public void close() {
        if (closer != null) {
            try {
                closer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public FlintModuleContainer getContainer() {
        return this.metadata;
    }

    public ResourcePackActivationType getActivationType() {
        return this.activationType;
    }

    @Override
    public @NotNull String packId() {
        return this.id;
    }

    private static boolean exists(Path path) {
        // NIO Files.exists is notoriously slow when checking the file system
        return path.getFileSystem() == DEFAULT_FS ? path.toFile().exists() : Files.exists(path);
    }

    private static String getFilename(PackType type, ResourceLocation id) {
        return String.format(Locale.ROOT, "%s/%s/%s", type.getDirectory(), id.getNamespace(), id.getPath());
    }
}
