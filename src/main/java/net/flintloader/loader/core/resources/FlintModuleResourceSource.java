package net.flintloader.loader.core.resources;

import net.flintloader.loader.FlintConstants;
import net.flintloader.loader.modules.FlintModuleMetadata;
import net.flintloader.loader.modules.ModuleList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FlintModuleResourceSource implements RepositorySource {

    public FlintModuleResourceSource() {}

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        /*for (FlintModuleMetadata module : ModuleList.getInstance().allModules()) {
            if (module.isBuiltIn()) continue;

            try {
                discoverPacks(module.getSource(), ((path, resourcesSupplier) -> {
                    Pack.Info info = new Pack.Info(Component.literal("Flint Modules"), PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), new ArrayList<>());
                    Pack pack = Pack.create("flint", Component.literal("Flint Modules"), true, resourcesSupplier, info, Pack.Position.TOP, true, PackSource.BUILT_IN);
                    consumer.accept(pack);
                }));
            } catch (Exception e) {
                FlintConstants.LOG.warn("Failed to list packs in {}", module.getName(), e);
            }
        }*/
    }

    /*private void discoverPacks(Path path, BiConsumer<Path, Pack.ResourcesSupplier> biConsumer) {
        Pack.ResourcesSupplier mainSupplier = detectPackResources(path);
        if (mainSupplier != null) {
            biConsumer.accept(path, mainSupplier);
        }
    }*/

    /*public static Pack.ResourcesSupplier detectPackResources(Path path) {
        BasicFileAttributes attributes;
        try {
            attributes = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (NoSuchFileException e) {
            return null;
        } catch (IOException e) {
            FlintConstants.LOG.warn("Failed to read properties of '{}', ignoring", path, e);
            return null;
        }

        if (attributes.isDirectory() && Files.isRegularFile(path.resolve("pack.mcmeta"))) {
            return string -> new PathPackResources(string, path, true);
        } else {
            if (attributes.isRegularFile() && path.getFileName().toString().endsWith(".jar")) {
                FileSystem fileSystem = path.getFileSystem();
                if (fileSystem == FileSystems.getDefault() || fileSystem instanceof LinkFileSystem) {
                    File file = path.toFile();
                    return string -> new FilePackResources(string, file, true, "");
                }
            }
        }

        FlintConstants.LOG.warn("Found non-pack entry '{}', ignoring", path);
        return null;
    }*/
}
