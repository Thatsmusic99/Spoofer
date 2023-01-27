package io.github.thatsmusic99.spoofer.api;

import com.destroystokyo.paper.entity.ai.Goal;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public interface ISpoofedPlayer extends Player, Creature {

    Player getBukkitPlayer();

    Plugin getSpawningPlugin();

    Set<Audience> getChatListeners();

    void addChatListener(Audience audience);

    void removeChatListener(Audience audience);

    void addGoal(int priority, Goal<ISpoofedPlayer> goal);

    void removeGoal(Goal<ISpoofedPlayer> goal);
}
