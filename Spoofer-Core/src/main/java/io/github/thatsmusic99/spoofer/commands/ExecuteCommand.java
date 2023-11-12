package io.github.thatsmusic99.spoofer.commands;

import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import io.github.thatsmusic99.spoofer.api.PlayerTracker;
import io.github.thatsmusic99.spoofer.Spoofer;
import io.github.thatsmusic99.spoofer.Utilities;
import io.github.thatsmusic99.spoofer.api.IFakePlayer;
import io.github.thatsmusic99.spoofer.api.SpooferHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExecuteCommand implements ISubCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 1) {
            Spoofer.sendWarnMessage(sender, "A player name must be specified for this command.");
            return false;
        }

        ISpoofedPlayer player = SpooferHandler.getAPI().getPlayer(args[1]);
        if (player == null) {
            Spoofer.sendWarnMessage(sender, "You need to specify a spoofed player to use this subcommand.");
            return false;
        }

        if (args.length < 3) {
            Spoofer.sendWarnMessage(sender, "You need to specify a command to run.");
            return false;
        }

        String commandStr = Utilities.getStringFromArray(args, 2);
        if (player.getBukkitPlayer().performCommand(commandStr)) {
            Spoofer.sendMessage(sender, "Player " + args[1] + " has executed the command \"" + commandStr + "\".");
        } else {
            Spoofer.sendWarnMessage(sender, "Player " + args[1] + " has executed the command \"" + commandStr + "\", but the command did not succeed.");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
