package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.api.SpooferHandler;
import io.github.thatsmusic99.spoofer.commands.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Spoofer extends JavaPlugin {

    private static Spoofer instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("spoofer").setExecutor(this);
        instance = this;

        // Plugin API
        SpooferImpl impl = new SpooferImpl();
        SpooferHandler.setInstance(impl);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Spoofer get() {
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {

        if (args.length == 0) return false;
        switch (args[0].toLowerCase()) {
            case "add":
                new AddCommand().onCommand(sender, command, label, args);
                break;
            case "execute":
                new ExecuteCommand().onCommand(sender, command, label, args);
                break;
            case "chat":
                new ChatCommand().onCommand(sender, command, label, args);
                break;
            case "listen":
                new ListenCommand().onCommand(sender, command, label, args);
                break;
            case "bulk":
                new BulkCommand().onCommand(sender, command, label, args);
                break;
        }
        return true;
    }

    public static void sendWarnMessage(CommandSender sender, String message) {
        sender.sendMessage(Component.text("Spoofer").color(TextColor.color(0xCC5B5A))
                .append(Component.text(" > ").color(TextColor.color(0x383838)))
                .append(Component.text(message).color(TextColor.color(0xCC9B9C))));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(Component.text("Spoofer").color(TextColor.color(0xCC5B5A))
                .append(Component.text(" > ").color(TextColor.color(0x383838)))
                .append(Component.text(message).color(TextColor.color(0xC7E7FF))));
    }

    public static void sendMessage(Audience sender, String context, Component message) {
        sender.sendMessage(Component.text(context).color(TextColor.color(0xCC5B5A))
                .append(Component.text(" > ").color(TextColor.color(0x383838)))
                .append(message));
    }

    public static void sendMessage(Audience sender, String context, String message) {
        sender.sendMessage(Component.text(context).color(TextColor.color(0xCC5B5A))
                .append(Component.text(" > ").color(TextColor.color(0x383838)))
                .append(Component.text(message).color(TextColor.color(0xC7E7FF))));
    }
}
