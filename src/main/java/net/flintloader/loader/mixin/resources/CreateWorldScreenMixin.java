package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.ModuleResourcePackCreator;
import net.flintloader.loader.core.resources.ModuleResourcePackUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.WorldDataConfiguration;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {

    @Shadow @Nullable private PackRepository tempDataPackRepository;

    private CreateWorldScreenMixin() {
        super(null);
    }

    @ModifyVariable(method = "openFresh", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;createDefaultLoadConfig(Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/world/level/WorldDataConfiguration;)Lnet/minecraft/server/WorldLoader$InitConfig;"))
    private static PackRepository onCreateResManagerInit(PackRepository packRepository) {
        packRepository.sources.add(new ModuleResourcePackCreator(PackType.SERVER_DATA));
        return packRepository;
    }

    @Redirect(method = "createLevelSettings", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/WorldDataConfiguration;DEFAULT:Lnet/minecraft/world/level/WorldDataConfiguration;"))
    private WorldDataConfiguration replaceDefaultSettings() {
        return ModuleResourcePackUtil.createDefaultDataConfiguration();
    }

    @Inject(method = "getDataPackSelectionSettings", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;reload()V", shift = At.Shift.BEFORE))
    private void onScanPacks(CallbackInfoReturnable<Pair<File, PackRepository>> cir) {
        this.tempDataPackRepository.sources.add(new ModuleResourcePackCreator(PackType.SERVER_DATA));
    }
}
