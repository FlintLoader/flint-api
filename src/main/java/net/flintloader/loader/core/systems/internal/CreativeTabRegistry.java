package net.flintloader.loader.core.systems.internal;

import net.flintloader.loader.api.creativetab.FlintCreativeModeTab;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author HypherionSA
 * A helper class to make registering creative tabs easier across modloaders
 */
public class CreativeTabRegistry {

    private static final List<FlintCreativeModeTab> TABS = new ArrayList<>();
    private static final List<Pair<FlintCreativeModeTab, Supplier<? extends ItemLike>>> TAB_ITEMS = new ArrayList<>();

    public static void setCreativeTab(FlintCreativeModeTab tab, Supplier<? extends ItemLike> item) {
        if (item != null) {
            TAB_ITEMS.add(Pair.of(tab, item));
        }
    }

    public static void registerTab(FlintCreativeModeTab tab) {
        TABS.add(tab);
    }

    public static List<FlintCreativeModeTab> getTabs() {
        return TABS;
    }

    public static List<Pair<FlintCreativeModeTab, Supplier<? extends ItemLike>>> getTabItems() {
        return TAB_ITEMS;
    }
}
