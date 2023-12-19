package net.flintloader.loader.core.hooks;

import net.flintloader.loader.modules.ModuleList;
import net.flintloader.punch.impl.PunchLoaderImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class LoaderHooks {

    public static void injectLoaderBranding(GuiGraphics guiGraphics, Font font, int width, int height) {
        Component warning = Component.literal(ChatFormatting.RED + "Flint Loader Alpha: " + ChatFormatting.YELLOW + "Things may break!");
        guiGraphics.drawString(font, warning, (width - font.width(warning)) / 2, 10, -1);

        for(String str : getMainMenuStrings()) {
            guiGraphics.drawString(font, str, 2, height - 20 - (getMainMenuStrings().indexOf(str) * 10), -1);
        }
    }

    private static List<String> getMainMenuStrings() {
        List<String> list = new ArrayList<>();
        list.add(ModuleList.getInstance().getModuleCount() + " loaded module" + (ModuleList.getInstance().getModuleCount() == 1 ? "" : "s"));
        list.add("Flint Loader " + PunchLoaderImpl.VERSION);
        return list;
    }

}
