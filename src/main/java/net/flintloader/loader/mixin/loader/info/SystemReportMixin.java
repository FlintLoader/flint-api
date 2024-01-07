package net.flintloader.loader.mixin.loader.info;

import net.flintloader.loader.FlintLoader;
import net.flintloader.loader.api.FlintModuleContainer;
import net.minecraft.SystemReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;

@Mixin(SystemReport.class)
public abstract class SystemReportMixin {

    @Shadow public abstract void setDetail(String string, Supplier<String> supplier);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectSystemDetails(CallbackInfo ci) {
        setDetail("Flint Modules", () -> {
            ArrayList<FlintModuleContainer> topLevelModules = new ArrayList<>();

            for (FlintModuleContainer container : FlintLoader.getLoadedModules(true)) {
                if (container.getContainingModule().isEmpty())
                    topLevelModules.add(container);
            }

            StringBuilder modString = new StringBuilder();
            appendModules(modString, 2, topLevelModules);
            return modString.toString();
        });
    }

    private static void appendModules(StringBuilder moduleString, int depth, ArrayList<FlintModuleContainer> modules) {
        modules.sort(Comparator.comparing(module -> module.getMetadata().getId()));

        for (FlintModuleContainer container : modules) {
            moduleString.append('\n');
            moduleString.append("\t".repeat(depth));
            moduleString.append(container.getMetadata().getId());
            moduleString.append(": ");
            moduleString.append(container.getMetadata().getName());
            moduleString.append(' ');
            moduleString.append(container.getMetadata().getVersion());

            // TODO Nested Modules
        }
    }
}
