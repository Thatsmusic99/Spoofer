package io.github.thatsmusic99.spoofer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class Utilities {

    public static GameProfile determineProfile(String name) {

        return new GameProfile(UUID.nameUUIDFromBytes(name.getBytes()), name);
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
                method = getServer().getClass().getMethod("getPlayerList");
                return (PlayerList) method.invoke(getServer());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
