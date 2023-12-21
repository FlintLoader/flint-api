package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.ModuleResourcePackUtil;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.WorldDataConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DedicatedServerProperties.class)
public class DedicatedServerPropertiesMixin {

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/WorldDataConfiguration;DEFAULT:Lnet/minecraft/world/level/WorldDataConfiguration;"))
    private WorldDataConfiguration replaceDefaultConfiguration() {
        return ModuleResourcePackUtil.createDefaultDataConfiguration();
    }

}
