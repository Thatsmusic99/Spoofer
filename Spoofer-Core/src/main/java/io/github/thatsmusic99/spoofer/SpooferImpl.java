package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.api.ISpoofedConnection;
import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import io.github.thatsmusic99.spoofer.api.SpooferAPI;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpooferImpl implements SpooferAPI {

    private final HashMap<String, ISpoofedPlayer> players;

    public SpooferImpl() {
        players = new HashMap<>();
    }

    @Override
    public @NotNull Set<ISpoofedPlayer> getPlayers() {
        return new HashSet<>(players.values());
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return Spoofer.get();
    }

    @Override
    public @NotNull ISpoofedConnection createEmptyConnection() {
        return null;
    }

    @Override
    public @Nullable ISpoofedPlayer getPlayer(String name) {
        return players.get(name);
    }

    @Override
    public @NotNull ISpoofedPlayer addPlayer(@Nullable UUID uuid, @NotNull String name, @NotNull Plugin plugin) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> nmsClass = Class.forName("io.github.thatsmusic99.spoofer.NMSUtilities");
        Method method = nmsClass.getDeclaredMethod("createSpoofedPlayer", Plugin.class, String.class);
        ISpoofedPlayer player = (ISpoofedPlayer) method.invoke(null, plugin, name);
        players.put(name, player);
        return player;
    }
}
