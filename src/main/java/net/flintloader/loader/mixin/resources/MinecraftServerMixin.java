package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.BuiltInModuleResourcePackSource;
import net.flintloader.loader.core.resources.ModuleNioResoucePack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Redirect(method = "configurePackRepository", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"))
    private static boolean onCheckDisabled(List<String> list, Object o, PackRepository resourcePackManager) {
        String profileName = (String) o;
        boolean contains = list.contains(profileName);

        if (contains) {
            return true;
        }

        Pack profile = resourcePackManager.getPack(profileName);

        if (profile.getPackSource() instanceof BuiltInModuleResourcePackSource) {
            try (PackResources pack = profile.open()) {
                // Prevents automatic load for built-in data packs provided by mods.
                return pack instanceof ModuleNioResoucePack modPack && !modPack.getActivationType().isEnabledByDefault();
            }
        }

        return false;
    }

}
