package io.github.thatsmusic99.spoofer.api;

import org.bukkit.command.CommandSender;

@Deprecated
public interface IFakePlayer {

    boolean runCommand(String command);

    void chat(String message);

    void addChatListener(CommandSender sender);
}
