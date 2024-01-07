/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.creativetab;

import lombok.Getter;
import lombok.Setter;
import net.flintloader.loader.registry.CreativeTabRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * @author HypherionSA
 * Helper class to create custom creative tabs from modules
 */
public class FlintCreativeModeTab implements Supplier<CreativeModeTab> {

    @Getter private final ResourceLocation resourceLocation;
    @Getter private final Supplier<ItemStack> icon;
    @Getter private final String backgroundSuffix;
    @Getter @Setter private CreativeModeTab tab;
    @Getter private final ResourceKey<CreativeModeTab> resourceKey;

    protected FlintCreativeModeTab(Builder builder) {
        this.resourceLocation = builder.location;
        this.icon = builder.stack;
        this.backgroundSuffix = builder.backgroundSuffix == null ? "" : builder.backgroundSuffix;
        this.resourceKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, this.resourceLocation);

        CreativeTabRegistry.registerTab(this);
    }

    public static class Builder {
        private final ResourceLocation location;
        private Supplier<ItemStack> stack;
        private String backgroundSuffix;

        public Builder(ResourceLocation location) {
            this.location = location;
        }

        public Builder setIcon(Supplier<ItemStack> icon) {
            stack = icon;
            return this;
        }

        public Builder backgroundSuffix(String backgroundSuffix) {
            this.backgroundSuffix = backgroundSuffix;
            return this;
        }

        public FlintCreativeModeTab build() {
            return new FlintCreativeModeTab(this);
        }
    }

    @Override
    public CreativeModeTab get() {
        return tab == null ? CreativeModeTabs.getDefaultTab() : tab;
    }
}
