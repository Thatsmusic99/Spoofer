package io.github.thatsmusic99.spoofer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.List;

public class FakePlayer extends ServerPlayer {

    private ServerLevel world;
    private Connection connection;
    private SocketAddress bind;

    public FakePlayer(Location origin, String name) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        super(Utilities.getServer(), ((CraftWorld) origin.getWorld()).getHandle(), Utilities.determineProfile(name));
        world = ((CraftWorld) origin.getWorld()).getHandle();
        Spoofer.get().getLogger().info("Initiated FakePlayer");
        bind = new InetSocketAddress(InetAddress.getByName(Utilities.getServer().getLocalIp()), 28000);
        connection = new Connection(PacketFlow.CLIENTBOUND) {
            @Override
            public boolean isConnected() {
                return true;
            }

            @Override
            public boolean isConnecting() {
                return false;
            }

            @Override
            public void setReadOnly() {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return bind;
            }

            @Override
            public void send(Packet<?> packet) {
            }

            @Override
            public void send(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
            }
        };

        ServerConnectionListener listener = Utilities.getServer().getConnection();
        // Paper fix
        try {
            Field connectionsList = listener.getClass().getDeclaredField("g");
            connectionsList.setAccessible(true);
            List<Connection> connections = (List<Connection>) connectionsList.get(listener);
            connections.add(connection);
        } catch (NoSuchFieldError ex) {
            ex.printStackTrace();
        }

        Utilities.getPlayerList().placeNewPlayer(connection, this);
        Spoofer.get().getLogger().info("Placed new player");
        setPos(origin.getX(), origin.getY(), origin.getZ());
        Spoofer.get().getLogger().info("ree " + getStringUUID());
    }

    public void runCommand(String command) {
        getBukkitEntity().performCommand(command);
    }
}
