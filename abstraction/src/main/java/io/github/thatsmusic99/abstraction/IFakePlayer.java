package io.github.thatsmusic99.abstraction;

import lib.brainsynder.ServerVersion;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public interface IFakePlayer {
    void runCommand(String command);

    void addChatListener(CommandSender sender);

    void chat(String content);

    void teleportPlayer (Location location);

    void moveTo (Location location);

    static IFakePlayer spawnFakePlayer(Location origin, String name, Plugin plugin) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> target = Class.forName("io.github.thatsmusic99.versions." + ServerVersion.getVersion().name() + ".FakePlayer");
        return (IFakePlayer) target.getConstructor(Location.class, String.class, Plugin.class).newInstance(origin, name, plugin);
    }
}
