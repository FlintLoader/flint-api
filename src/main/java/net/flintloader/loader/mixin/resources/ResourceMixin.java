package net.flintloader.loader.mixin.resources;

import net.flintloader.loader.core.resources.FlintResource;
import net.flintloader.loader.core.resources.ResourcePackTracker;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Resource.class)
public class ResourceMixin implements FlintResource {
    @Override
    public PackSource getFlintPackSource() {
        Resource self = (Resource) (Object) this;
        return ResourcePackTracker.getSource(self.source());
    }
}
