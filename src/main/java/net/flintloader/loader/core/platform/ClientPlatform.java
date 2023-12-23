/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.core.platform;

import net.flintloader.loader.util.ServiceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 */
public class ClientPlatform {

    public final ClientPlatform INSTANCE = ServiceUtil.load(ClientPlatform.class);

    @NotNull
    public Minecraft getClientInstance() {
        return Minecraft.getInstance();
    }

    @Nullable
    public Player getClientPlayer() {
        return getClientInstance().player;
    }

    @Nullable
    public Level getClientLevel() {
        if (getClientPlayer() == null)
            return null;

        return getClientPlayer().level();
    }

    @Nullable
    public Connection getClientConnection() {
        if (getClientInstance().getConnection() == null)
            return null;

        return getClientInstance().getConnection().getConnection();
    }
}
