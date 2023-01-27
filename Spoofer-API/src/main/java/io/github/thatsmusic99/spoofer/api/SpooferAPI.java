package io.github.thatsmusic99.spoofer.api;

import io.github.thatsmusic99.spoofer.api.exceptions.PlayerAlreadyJoinedException;
import org.bukkit.plugin.Plugin;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;

public interface SpooferAPI {

    static SpooferAPI get() {
        return SpooferHandler.getAPI();
    }

    @NotNull Set<ISpoofedPlayer> getPlayers();

    @NotNull Plugin getPlugin();

    @NotNull ISpoofedConnection createEmptyConnection();

    @Nullable ISpoofedPlayer getPlayer(String name);

    @Contract(" _, _, _ -> new")
    @NotNull ISpoofedPlayer addPlayer(
            @Nullable UUID uuid,
            @Pattern("^[0-9a-zA-Z_]{3,16}$") @NotNull String name,
            @NotNull Plugin plugin
    ) throws PlayerAlreadyJoinedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
