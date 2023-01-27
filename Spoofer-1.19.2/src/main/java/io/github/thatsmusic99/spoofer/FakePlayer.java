package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.api.IFakePlayer;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.metadata.LazyMetadataValue;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;

public class FakePlayer extends ServerPlayer implements IFakePlayer {

    private final Connection connectionSpoof;
    private final String name;
    private final HashSet<CommandSender> senders;
    private PathfinderHelper helper = null;
    private boolean respawning;

    public FakePlayer(String name) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        super(NMSUtilities.getServer(), ((CraftWorld) NMSUtilities.getWorld()).getHandle(), NMSUtilities.determineProfile(name), null);
        this.name = name;
        this.respawning = false;
        this.senders = new HashSet<>();
        this.locale = "en_us";
        // this.helper = new PathfinderHelper(this);
        Spoofer.get().getLogger().info("Initiated io.github.thatsmusic99.spoofer.FakePlayer");
        connectionSpoof = new FakeConnection(this);

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
        Spoofer.get().getLogger().info(name + "[" + NMSUtilities.getServer().getLocalIp() + ":" + 28000 +
                "] spoofed in with entity id " + getId() + " at ([" + level.getWorld().getName() + "] " + getX() + ", " + getY() + ", " + getZ() + ")");

        // level.addFreshEntity(helper);

        // addGoal(0, new WaterAvoidingRandomStrollGoal(helper, 1.0));
        // addGoal(1, new LookAtPlayerGoal(helper, Player.class, 6.0f));
        // addGoal(2, new RandomLookAroundGoal(helper));
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

    @Override
    public void teleportTo(ServerLevel targetWorld, double x, double y, double z, float yaw, float pitch) {
        super.teleportTo(targetWorld, x, y, z, yaw, pitch);
        if (helper != null) {
            helper.moveTo(x, y, z, yaw, pitch);
            helper.pendTeleport();
        }
    }

    public boolean isRespawning() {
        return respawning;
    }

    public void setRespawning(boolean respawning) {
        this.respawning = respawning;
    }

    public HashSet<CommandSender> getSenders() {
        return senders;
    }
}
