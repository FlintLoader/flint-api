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

import net.flintloader.loader.registry.resources.FlintResource;
import net.flintloader.loader.registry.resources.ResourcePackTracker;
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
