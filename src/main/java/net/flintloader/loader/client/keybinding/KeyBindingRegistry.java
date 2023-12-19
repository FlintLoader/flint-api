package net.flintloader.loader.client.keybinding;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.flintloader.loader.mixin.accessor.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeyBindingRegistry {
    private static final List<KeyMapping> KEY_MAPPINGS = new ReferenceArrayList<>();

    private KeyBindingRegistry() {}

    private static Map<String, Integer> getCategoryMap() {
        return KeyMappingAccessor.flint_getCategoryMap();
    }

    public static boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = getCategoryMap();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
        int largestInt = largest.orElse(0);
        map.put(categoryTranslationKey, largestInt + 1);
        return true;
    }

    public static KeyMapping registerKeyBinding(KeyMapping binding) {
        if (Minecraft.getInstance().options != null) {
            throw new IllegalStateException("GameOptions has already been initialised");
        }

        for (KeyMapping existingKeyBindings : KEY_MAPPINGS) {
            if (existingKeyBindings == binding) {
                throw new IllegalArgumentException("Attempted to register a key binding twice: " + binding.getName());
            } else if (existingKeyBindings.getName().equals(binding.getName())) {
                throw new IllegalArgumentException("Attempted to register two key bindings with equal ID: " + binding.getName() + "!");
            }
        }

        // This will do nothing if the category already exists.
        addCategory(binding.getCategory());
        KEY_MAPPINGS.add(binding);
        return binding;
    }

    public static KeyMapping[] process(KeyMapping[] keysAll) {
        List<KeyMapping> newKeysAll = Lists.newArrayList(keysAll);
        newKeysAll.removeAll(KEY_MAPPINGS);
        newKeysAll.addAll(KEY_MAPPINGS);
        return newKeysAll.toArray(new KeyMapping[0]);
    }

}
