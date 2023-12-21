package net.flintloader.loader.core.resources;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.jetbrains.annotations.NotNull;

public class BuiltInModuleResourcePackSource implements PackSource {

    private final String moduleId;

    public BuiltInModuleResourcePackSource(String moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean shouldAddAutomatically() {
        return true;
    }

    @Override
    public @NotNull Component decorate(Component component) {
        return Component.translatable("pack.nameAndSource", component, Component.translatable("pack.source.builtinModule", moduleId)).withStyle(ChatFormatting.GRAY);
    }
}
