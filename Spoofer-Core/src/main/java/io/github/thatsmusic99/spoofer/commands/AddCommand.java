package io.github.thatsmusic99.spoofer.commands;

import io.github.thatsmusic99.spoofer.Spoofer;
import io.github.thatsmusic99.spoofer.api.SpooferHandler;
import io.github.thatsmusic99.spoofer.api.exceptions.PlayerAlreadyJoinedException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AddCommand implements ISubCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @Subst("SPOOFED_1") @NotNull String[] args) {
        if (args.length == 1) {
            Spoofer.sendWarnMessage(sender, "A player name must be specified for this command.");
            return false;
        }

        try {
            SpooferHandler.getAPI().addPlayer(null, args[1], Spoofer.get());
            Spoofer.sendMessage(sender, "Added " + args[1] + " to the server.");
        } catch (IllegalAccessException e) {
            Spoofer.sendWarnMessage(sender, "Failed to create the fake player: could not access the fake player constructor.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Spoofer.sendWarnMessage(sender, "Failed to create the fake player: the player class was not found. Do not use the core plugin on its own.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Spoofer.sendWarnMessage(sender, "Failed to create the fake player: something went wrong initialising the player. Please report this to the developer.");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Spoofer.sendWarnMessage(sender, "Failed to create the fake player: could not find the fake player constructor.");
            e.printStackTrace();
        } catch (InstantiationException e) {
            Spoofer.sendWarnMessage(sender, "Failed to create the fake player: could not initialise the fake player.");
            e.printStackTrace();
        } catch (PlayerAlreadyJoinedException e) {
            throw new RuntimeException(e);
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
