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

import net.flintloader.loader.api.resources.ModuleResourcePack;
import net.flintloader.loader.registry.resources.FlintWrappedVanillaResourcePack;
import net.flintloader.loader.registry.resources.ModuleResourcePackUtil;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPackSource.class)
public class ClientPackSourceMixin {

    @ModifyArg(
            method = "createBuiltinPack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/Pack;readMetaAndCreate(Ljava/lang/String;Lnet/minecraft/network/chat/Component;ZLnet/minecraft/server/packs/repository/Pack$ResourcesSupplier;Lnet/minecraft/server/packs/PackType;Lnet/minecraft/server/packs/repository/Pack$Position;Lnet/minecraft/server/packs/repository/PackSource;)Lnet/minecraft/server/packs/repository/Pack;"),
            index = 3
    )
    private Pack.ResourcesSupplier onCreateVanillaBuiltInResourcePack(String name, Component displayName, boolean alwaysEnabled, Pack.ResourcesSupplier resourcesSupplier, PackType packType, Pack.Position position, PackSource packSource) {
        return new Pack.ResourcesSupplier() {
            @Override
            public @NotNull PackResources openPrimary(String name) {
                return new FlintWrappedVanillaResourcePack((AbstractPackResources) resourcesSupplier.openPrimary(name), getModuleResourcePacks(name));
            }

            @Override
            public @NotNull PackResources openFull(String string, Pack.Info info) {
                return openPrimary(string);
            }
        };
    }

    private static List<ModuleResourcePack> getModuleResourcePacks(String subPath) {
        List<ModuleResourcePack> packs = new ArrayList<>();
        ModuleResourcePackUtil.appendModuleResourcePacks(packs, PackType.CLIENT_RESOURCES, subPath);
        return packs;
    }

}
