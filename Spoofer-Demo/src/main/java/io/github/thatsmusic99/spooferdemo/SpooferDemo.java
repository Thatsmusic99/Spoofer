package io.github.thatsmusic99.spooferdemo;

import com.destroystokyo.paper.entity.ai.VanillaGoal;
import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import io.github.thatsmusic99.spoofer.api.SpooferAPI;
import io.github.thatsmusic99.spoofer.api.exceptions.PlayerAlreadyJoinedException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SpooferDemo extends JavaPlugin {

    private static SpooferDemo instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static SpooferDemo get() {
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // If the sender isn't a player, stop there
        if (!(sender instanceof Player playerSender)) return false;

        // Adds
        try {
            ISpoofedPlayer player = SpooferAPI.get().addPlayer(null, "SpooferTester", this);
            player.addGoal(0, );
        } catch (PlayerAlreadyJoinedException e) {

        }
        return super.onCommand(sender, command, label, args);
    }
}
