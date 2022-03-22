package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.abstraction.IFakePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

public final class Spoofer extends JavaPlugin {

    private HashMap<String, IFakePlayer> fakePlayers;
    private static Spoofer instance;

    @Override
    public void onEnable() {
        getCommand("spoofer").setExecutor(this);
        instance = this;
        fakePlayers = new HashMap<>();
    }

    @Override
    public void onDisable() {
    }

    public static Spoofer get() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 1) return false;

            try {
                IFakePlayer pl = IFakePlayer.spawnFakePlayer(player.getLocation(), args[1], this);
                fakePlayers.put(args[1], pl);
            } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("command")) {
            if (args.length == 1) return false;

            String name = args[1];
            if (!fakePlayers.containsKey(name)) return false;

            IFakePlayer fakePlayer = fakePlayers.get(name);
            fakePlayer.runCommand(getStringFromArray(args, 2));
            return true;
        }

        if (args[0].equalsIgnoreCase("chat")) {
            if (args.length == 1) return false;

            String name = args[1];
            if (!fakePlayers.containsKey(name)) return false;

            IFakePlayer fakePlayer = fakePlayers.get(name);
            fakePlayer.chat(getStringFromArray(args, 2));
            return true;
        }

        if (args[0].equalsIgnoreCase("listen")) {
            if (args.length == 1) return false;

            String name = args[1];
            if (!fakePlayers.containsKey(name)) return false;

            IFakePlayer fakePlayer = fakePlayers.get(name);
            fakePlayer.addChatListener(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("tphere")) {
            if (args.length == 1) return false;

            String name = args[1];
            if (!fakePlayers.containsKey(name)) return false;

            IFakePlayer fakePlayer = fakePlayers.get(name);
            fakePlayer.teleportPlayer(player.getLocation());
            return true;
        }

        if (args[0].equalsIgnoreCase("movehere")) {
            if (args.length == 1) return false;

            String name = args[1];
            if (!fakePlayers.containsKey(name)) return false;

            IFakePlayer fakePlayer = fakePlayers.get(name);
            fakePlayer.moveTo(player.getLocation());
            return true;
        }
        return false;
    }

    public static String getStringFromArray(String[] args, int start) {
        String[] neededParts = Arrays.copyOfRange(args, start, args.length);
        return String.join(" ", neededParts);
    }
}
