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

public class BulkCommand implements ISubCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @Subst("SPOOFED_") @NotNull String[] args) {

        // See if there has been a number specified
        if (args.length == 1 || !args[1].matches("^[0-9]+$")) {
            Spoofer.sendWarnMessage(sender, "The number of players to spawn must be specified.");
            return false;
        }

        int count = Integer.parseInt(args[1]);
        int players = 0;

        for (int i = 0; i < count; i++) {
            try {
                SpooferHandler.getAPI().addPlayer(null, "Player" + i, Spoofer.get());
                players++;
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
        }

        Spoofer.sendMessage(sender, "Successfully spawned " + players + " players.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
