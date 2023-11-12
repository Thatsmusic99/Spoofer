package io.github.thatsmusic99.spoofer.craft;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import io.github.thatsmusic99.spoofer.SpoofedPlayer;
import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class CraftSpoofedPlayer extends CraftPlayer implements ISpoofedPlayer {

    private final SpoofedPlayer player;
    private final HashSet<Audience> senders;
    private final Plugin spawningPlugin;

    public CraftSpoofedPlayer(Plugin plugin, SpoofedPlayer player) {
        super((CraftServer) Bukkit.getServer(), player);

        this.player = (SpoofedPlayer) entity;
        this.spawningPlugin = plugin;
        this.senders = new HashSet<>();
    }

    public SpoofedPlayer getSpoofedPlayer() {
        return player;
    }

    @Override
    public boolean teleport(@NotNull Location location) {

        // If we're going to a different world, then respawn the pathfinder helper
        if (location.getWorld() != getWorld()) {
            player.getHelper().remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
            boolean b = super.teleport(location);
            player.respawnHelper();
            return b;
        }

        return player.getHelper().getBukkitEntity().teleport(location);
    }

    @Override
    public Player getBukkitPlayer() {
        return this;
    }

    @Override
    public Plugin getSpawningPlugin() {
        return spawningPlugin;
    }

    @Override
    public Set<Audience> getChatListeners() {
        return senders;
    }

    @Override
    public void addChatListener(Audience audience) {
        senders.add(audience);
    }

    @Override
    public void removeChatListener(Audience audience) {
        senders.remove(audience);
    }

    @Override
    public void addGoal(int priority, Goal<ISpoofedPlayer> goal) {
        Bukkit.getMobGoals().addGoal(this, priority, goal);
    }

    @Override
    public void removeGoal(Goal<ISpoofedPlayer> goal) {
        Bukkit.getMobGoals().removeGoal(this, goal);
    }

    @Override
    public @NotNull Pathfinder getPathfinder() {
        return player.getHelper().getBukkitMob().getPathfinder();
    }

    @Override
    public boolean isInDaylight() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().isInDaylight();
    }

    @Override
    public void lookAt(@NotNull Location location) {
        getSpoofedPlayer().getHelper().getBukkitHelper().lookAt(location);
    }

    @Override
    public void lookAt(@NotNull Location location, float headRotationSpeed, float maxHeadPitch) {
        getSpoofedPlayer().getHelper().getBukkitHelper().lookAt(location, headRotationSpeed, maxHeadPitch);
    }

    @Override
    public void lookAt(@NotNull Entity entity) {
        getSpoofedPlayer().getHelper().getBukkitHelper().lookAt(entity);
    }

    @Override
    public void lookAt(@NotNull Entity entity, float headRotationSpeed, float maxHeadPitch) {
        getSpoofedPlayer().getHelper().getBukkitHelper().lookAt(entity, headRotationSpeed, maxHeadPitch);
    }

    @Override
    public void lookAt(double x, double y, double z) {
        getSpoofedPlayer().getHelper().getBukkitHelper().lookAt(x, y, z);
    }

    @Override
    public void lookAt(double x, double y, double z, float headRotationSpeed, float maxHeadPitch) {
        getSpoofedPlayer().getHelper().getBukkitHelper().lookAt(x, y, z, headRotationSpeed, maxHeadPitch);
    }

    @Override
    public int getHeadRotationSpeed() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().getHeadRotationSpeed();
    }

    @Override
    public int getMaxHeadPitch() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().getMaxHeadPitch();
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        getSpoofedPlayer().getHelper().getBukkitHelper().setTarget(target);
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().getTarget();
    }

    @Override
    public void setAware(boolean aware) {
        getSpoofedPlayer().getHelper().getBukkitHelper().setAware(true);
    }

    @Override
    public boolean isAware() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().isAware();
    }

    @Override
    public @Nullable Sound getAmbientSound() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().getAmbientSound();
    }

    @Override
    public boolean isLeftHanded() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().isLeftHanded();
    }

    @Override
    public void setLeftHanded(boolean leftHanded) {
        getSpoofedPlayer().getHelper().getBukkitHelper().setLeftHanded(leftHanded);
    }

    @Override
    public int getPossibleExperienceReward() {
        return 0;
    }

    @Override
    public void setLootTable(@Nullable LootTable table) {
        getSpoofedPlayer().getHelper().getBukkitHelper().setLootTable(table);
    }

    @Override
    public @Nullable LootTable getLootTable() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().getLootTable();
    }

    @Override
    public void setSeed(long seed) {
        getSpoofedPlayer().getHelper().getBukkitHelper().setSeed(seed);
    }

    @Override
    public long getSeed() {
        return getSpoofedPlayer().getHelper().getBukkitHelper().getSeed();
    }
}
