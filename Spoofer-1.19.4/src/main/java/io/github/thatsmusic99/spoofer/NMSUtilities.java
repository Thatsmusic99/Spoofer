package io.github.thatsmusic99.spoofer;

import com.mojang.authlib.GameProfile;
import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.UUID;

public class NMSUtilities {

    public static GameProfile determineProfile(String name) {
        return new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()), name);
    }

    public static DedicatedServer getServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    public static PlayerList getPlayerList() {
        try {
            return getServer().getPlayerList();
        } catch (NoSuchMethodError ex) {
            Method method;
            try {
                // Paper missing mappings
                method = getServer().getClass().getMethod("getPlayerList");
                return (PlayerList) method.invoke(getServer());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ISpoofedPlayer createSpoofedPlayer(Plugin plugin, String name) throws UnknownHostException, NoSuchFieldException, IllegalAccessException {
        SpoofedPlayer player = new SpoofedPlayer(plugin, name);
        return player.getCraftSpoofedPlayer();
    }

    public static World getWorld() {
        for (World world : Bukkit.getWorlds()) {
            return world;
        }
        return null;
    }
}
