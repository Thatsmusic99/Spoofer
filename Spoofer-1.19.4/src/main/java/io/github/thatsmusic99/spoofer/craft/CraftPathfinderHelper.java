package io.github.thatsmusic99.spoofer.craft;

import io.github.thatsmusic99.spoofer.PathfinderHelper;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftHusk;

public class CraftPathfinderHelper extends CraftHusk {

    private final CraftSpoofedPlayer player;
    public CraftPathfinderHelper(PathfinderHelper entity, CraftSpoofedPlayer player) {
        super((CraftServer) Bukkit.getServer(), entity);

        this.player = player;
    }
}
