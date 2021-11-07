package io.github.thatsmusic99.spoofer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import javax.annotation.Nullable;

public class FakePlayer extends ServerPlayer {

    private ServerLevel world;
    private Connection connection;

    public FakePlayer(Location origin, String name) {
        super(Utilities.getServer(), ((CraftWorld) origin.getWorld()).getHandle(), Utilities.determineProfile(name));
        world = ((CraftWorld) origin.getWorld()).getHandle();
        Spoofer.get().getLogger().info("Initiated FakePlayer");
        connection = new Connection(PacketFlow.CLIENTBOUND) {
            @Override
            public boolean isConnected() {
                return true;
            }

            @Override
            public void send(Packet<?> packet) {
            }

            @Override
            public void send(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
            }
        };
        Utilities.getPlayerList().placeNewPlayer(connection, this);
        Spoofer.get().getLogger().info("Placed new player");
        setPos(origin.getX(), origin.getY(), origin.getZ());
        Spoofer.get().getLogger().info("ree " + getStringUUID());
    }

    public void runCommand(String command) {
        getBukkitEntity().performCommand(command);
    }
}
