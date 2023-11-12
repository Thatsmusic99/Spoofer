package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.craft.CraftSpoofedPlayer;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpoofedPlayer extends ServerPlayer {

    private final Connection connectionSpoof;
    private final String name;
    private final Set<Audience> senders;
    private final CraftSpoofedPlayer craftSpoofedPlayer;
    private PathfinderHelper helper;
    private boolean respawning;

    public SpoofedPlayer(Plugin plugin, String name) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        super(NMSUtilities.getServer(), ((CraftWorld) NMSUtilities.getWorld()).getHandle(), NMSUtilities.determineProfile(name));

        // Initialise variables
        this.name = name;
        this.respawning = false;
        this.senders = new HashSet<>();
        this.locale = "en_us";
        this.craftSpoofedPlayer = new CraftSpoofedPlayer(plugin, this);
        this.helper = new PathfinderHelper(this);
        this.connectionSpoof = new FakeConnection(craftSpoofedPlayer);

        addPlayer();

        level.addFreshEntity(helper);

        // addGoal(0, new WaterAvoidingRandomStrollGoal(helper, 1.0));
        // addGoal(1, new LookAtPlayerGoal(helper, Player.class, 6.0f));
        // addGoal(2, new RandomLookAroundGoal(helper));
    }

    private void addPlayer() throws NoSuchFieldException, IllegalAccessException {

        ServerConnectionListener listener = NMSUtilities.getServer().getConnection();
        // Paper fix
        Field connectionsList;
        try {
            connectionsList = listener.getClass().getDeclaredField("g");
        } catch (NoSuchFieldError | NoSuchFieldException ex) {
            connectionsList = listener.getClass().getDeclaredField("connections");
        }
        connectionsList.setAccessible(true);
        List<Connection> connections = (List<Connection>) connectionsList.get(listener);
        connections.add(connectionSpoof);

        NMSUtilities.getPlayerList().placeNewPlayer(connectionSpoof, this);
        getBukkitEntity().setMetadata("spoofed", new LazyMetadataValue(Spoofer.get(), () -> true));
    }

    public boolean runCommand(String command) {
        Spoofer.get().getLogger().info(name + " is running command " + command);
        return getBukkitEntity().performCommand(command);
    }

    public void addChatListener(CommandSender sender) {
        senders.add(sender);
    }

    public void chat(String content) {
        getBukkitEntity().chat(content);
    }

    public void addGoal(int priority, Goal goal) {
        helper.goalSelector.addGoal(priority, goal);
    }

    public PathfinderHelper getHelper() {
        return helper;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public void setRespawning(boolean respawning) {
        this.respawning = respawning;
    }

    public Player getBukkitPlayer() {
        return craftSpoofedPlayer;
    }

    @Override
    public @NotNull CraftPlayer getBukkitEntity() {
        return craftSpoofedPlayer;
    }

    public @NotNull CraftSpoofedPlayer getCraftSpoofedPlayer() {
        return craftSpoofedPlayer;
    }

    public void respawnHelper() {
        helper = new PathfinderHelper(this);
        level.addFreshEntity(helper);
    }

    @Override
    public void disconnect() {
        super.disconnect();
        helper.remove(RemovalReason.DISCARDED);
    }
}
