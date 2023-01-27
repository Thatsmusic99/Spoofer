package io.github.thatsmusic99.spoofer.craft;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import io.github.thatsmusic99.spoofer.SpoofedPlayer;
import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class CraftSpoofedPlayer extends CraftPlayer {

    private final SpoofedPlayer player;

    public CraftSpoofedPlayer(SpoofedPlayer player) {
        super((CraftServer) Bukkit.getServer(), player);

        this.player = player;
    }

    public SpoofedPlayer getSpoofedPlayer() {
        return player;
    }

    @Override
    public boolean teleport(Location location) {
        return player.getHelper().getBukkitEntity().teleport(location);
    }
}
