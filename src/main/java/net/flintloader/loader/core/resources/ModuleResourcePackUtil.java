package net.flintloader.loader.core.resources;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.flintloader.loader.api.FlintModuleContainer;
import net.flintloader.loader.api.resources.ModuleResourcePack;
import net.flintloader.loader.api.resources.ResourcePackActivationType;
import net.flintloader.loader.modules.FlintModuleMetadata;
import net.flintloader.loader.modules.ModuleList;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;

public final class ModuleResourcePackUtil {
    private static final Gson GSON = new Gson();

    private ModuleResourcePackUtil() {}

    public static void appendModuleResourcePacks(List<ModuleResourcePack> packs, PackType type, @Nullable String subPath) {
        for (FlintModuleContainer container : ModuleList.getInstance().allModules()) {
            FlintModuleMetadata meta = container.getMetadata();
            if (meta.isBuiltIn()) continue;

            ModuleResourcePack pack = ModuleNioResoucePack.create(meta.getId(), container, subPath, type, ResourcePackActivationType.ALWAYS_ENABLED);

            if (pack != null) {
                packs.add(pack);
            }
        }
    }

    public static boolean containsDefault(FlintModuleContainer metadata, String filename) {
        return "pack.mcmeta".equals(filename);
    }

    public static InputStream openDefault(FlintModuleContainer metadata, PackType type, String filename) {
        if (filename.equals("pack.mcmeta")) {
            String description = Objects.requireNonNull(metadata.getMetadata().getName(), "");
            String meta = serializeMetadata(SharedConstants.getCurrentVersion().getPackVersion(type), description);
            return IOUtils.toInputStream(meta, Charsets.UTF_8);
        }
        return null;
    }

    public static String serializeMetadata(int packVersion, String description) {
        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", packVersion);
        pack.addProperty("description", description);
        JsonObject metadata = new JsonObject();
        metadata.add("pack", pack);
        return GSON.toJson(metadata);
    }

    public static Component getName(FlintModuleMetadata meta) {
        if (meta.getName() != null) {
            return Component.literal(meta.getName());
        }

        return Component.translatable("pack.name.flintModule", meta.getId());
    }

    public static WorldDataConfiguration createDefaultDataConfiguration() {
        ModuleResourcePackCreator modResourcePackCreator = new ModuleResourcePackCreator(PackType.SERVER_DATA);
        List<Pack> moddedResourcePacks = new ArrayList<>();
        modResourcePackCreator.loadPacks(moddedResourcePacks::add);

        List<String> enabled = new ArrayList<>(DataPackConfig.DEFAULT.getEnabled());
        List<String> disabled = new ArrayList<>(DataPackConfig.DEFAULT.getDisabled());

        // This ensures that any built-in registered data packs by mods which needs to be enabled by default are
        // as the data pack screen automatically put any data pack as disabled except the Default data pack.
        for (Pack profile : moddedResourcePacks) {
            try (PackResources pack = profile.open()) {
                if (pack instanceof FlintModuleResourcePack || (pack instanceof ModuleNioResoucePack && ((ModuleNioResoucePack) pack).getActivationType().isEnabledByDefault())) {
                    enabled.add(profile.getId());
                } else {
                    disabled.add(profile.getId());
                }
            }
        }

        return new WorldDataConfiguration(
                new DataPackConfig(enabled, disabled),
                FeatureFlags.DEFAULT_FLAGS
        );
    }

    public static DataPackConfig createTestServerSettings(List<String> enabled, List<String> disabled) {
        // Collect modded profiles
        Set<String> moddedProfiles = new HashSet<>();
        ModuleResourcePackCreator modResourcePackCreator = new ModuleResourcePackCreator(PackType.SERVER_DATA);
        modResourcePackCreator.loadPacks(profile -> moddedProfiles.add(profile.getId()));

        // Remove them from the enabled list
        List<String> moveToTheEnd = new ArrayList<>();

        for (Iterator<String> it = enabled.iterator(); it.hasNext();) {
            String profile = it.next();

            if (moddedProfiles.contains(profile)) {
                moveToTheEnd.add(profile);
                it.remove();
            }
        }

        // Add back at the end
        enabled.addAll(moveToTheEnd);

        return new DataPackConfig(enabled, disabled);
    }
}
