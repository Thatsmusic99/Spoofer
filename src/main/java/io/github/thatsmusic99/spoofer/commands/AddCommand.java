package io.github.thatsmusic99.spoofer.commands;

import io.github.thatsmusic99.spoofer.FakePlayer;
import io.github.thatsmusic99.spoofer.PlayerTracker;
import io.github.thatsmusic99.spoofer.Spoofer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.UnknownHostException;
import java.util.List;

public class AddCommand implements ISubCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 1) {
            Spoofer.sendWarnMessage(sender, "A player name must be specified for this command.");
            return false;
        }

        try {
            PlayerTracker.get().addPlayer(args[1], new FakePlayer(args[1]));
            Spoofer.sendMessage(sender, "Added " + args[1] + " to the server.");
        } catch (NoSuchFieldException | IllegalAccessException | UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      @NotNull String[] args) {

        return null;
    }
}
