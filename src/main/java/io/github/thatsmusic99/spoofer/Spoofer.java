package io.github.thatsmusic99.spoofer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.StringJoiner;

public final class Spoofer extends JavaPlugin {

    private HashMap<String, FakePlayer> fakePlayers;
    private static Spoofer instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("spoof").setExecutor(this);
        instance = this;
        fakePlayers = new HashMap<>();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Spoofer get() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 0) return false;
        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length == 1) return false;
                FakePlayer pl;
                try {
                    pl = new FakePlayer(player.getLocation(), args[1]);
                    fakePlayers.put(args[1], pl);
                } catch (NoSuchFieldException | IllegalAccessException | UnknownHostException e) {
                    e.printStackTrace();
                }
                break;
            case "command":
                if (args.length == 1) return false;
                String name = args[2];
                if (!fakePlayers.containsKey(name)) return false;
                FakePlayer fakePlayer = fakePlayers.get(name);

        }
        return true;
    }
}
