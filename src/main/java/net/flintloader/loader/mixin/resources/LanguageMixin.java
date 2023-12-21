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
