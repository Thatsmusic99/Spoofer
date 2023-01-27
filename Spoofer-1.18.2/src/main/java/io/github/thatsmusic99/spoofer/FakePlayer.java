package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.api.IFakePlayer;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.metadata.LazyMetadataValue;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;

public class FakePlayer extends ServerPlayer implements IFakePlayer {

    private final Connection connectionSpoof;
    private final String name;
    private final HashSet<CommandSender> senders;
    private boolean respawning;

    public FakePlayer(String name) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        super(NMSUtilities.getServer(), ((CraftWorld) NMSUtilities.getWorld()).getHandle(), NMSUtilities.determineProfile(name));
        this.name = name;
        this.respawning = false;
        this.senders = new HashSet<>();
        this.locale = "en_us";
        Spoofer.get().getLogger().info("Initiated FakePlayer");
        connectionSpoof = new FakeConnection(this);

        ServerConnectionListener listener = NMSUtilities.getServer().getConnection();
        // Paper fix
        try {
            Field connectionsList = listener.getClass().getDeclaredField("g");
            connectionsList.setAccessible(true);
            List<Connection> connections = (List<Connection>) connectionsList.get(listener);
            connections.add(connectionSpoof);
        } catch (NoSuchFieldError ex) {
            ex.printStackTrace();
        }

        NMSUtilities.getPlayerList().placeNewPlayer(connectionSpoof, this);
        getBukkitEntity().setMetadata("spoofed", new LazyMetadataValue(Spoofer.get(), () -> true));
        Spoofer.get().getLogger().info(name + "[" + NMSUtilities.getServer().getLocalIp() + ":" + 28000 +
                "] spoofed in with entity id " + getId() + " at ([" + level.getWorld().getName() + "] " + getX() + ", " + getY() + ", " + getZ() + ")");
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
