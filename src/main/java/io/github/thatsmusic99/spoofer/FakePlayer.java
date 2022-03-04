package io.github.thatsmusic99.spoofer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.metadata.LazyMetadataValue;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;

public class FakePlayer extends ServerPlayer {

    private final Connection connectionSpoof;
    private final SocketAddress bind;
    private final String name;
    private final HashSet<CommandSender> senders;
    private boolean respawning;

    public FakePlayer(String name) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        super(Utilities.getServer(), ((CraftWorld) Utilities.getWorld()).getHandle(), Utilities.determineProfile(name));
        this.name = name;
        this.respawning = false;
        this.senders = new HashSet<>();
        Spoofer.get().getLogger().info("Initiated FakePlayer");
        bind = new InetSocketAddress(InetAddress.getByName(Utilities.getServer().getLocalIp()), 28000);
        connectionSpoof = new Connection(PacketFlow.CLIENTBOUND) {
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
            public void send(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
                Spoofer.get().getLogger().info("Packet: " + packet.getClass().getSimpleName());
                if (packet instanceof ClientboundKeepAlivePacket) {
                    Spoofer.get().getLogger().info(name + " received the keep alive challenge, responding...");
                    connection.handleKeepAlive(new ServerboundKeepAlivePacket(((ClientboundKeepAlivePacket) packet).getId()));
                }

                if (isDeadOrDying() && !respawning) {
                    Spoofer.get().getLogger().info(name + " is dead, respawning in 20 ticks...");
                    respawning = true;
                    Bukkit.getScheduler().runTaskLater(Spoofer.get(), () -> {
                        respawning = false;
                        setHealth(20);
                        Utilities.getPlayerList().respawn(FakePlayer.this, false);
                    }, 20);
                }

                if (packet instanceof ClientboundChatPacket chatPacket) {
                    Spoofer.get().getLogger().info(String.valueOf(((ClientboundChatPacket) packet).getMessage()));
                    senders.forEach(sender -> sender.spigot().sendMessage(new ComponentBuilder(name).color(ChatColor.GRAY)
                            .append(" > ").color(ChatColor.DARK_GRAY).append(getPacketContent(chatPacket)).create()));

                }

                if (packet instanceof ClientboundSetEntityMotionPacket movePacket) {
                    if (movePacket.getId() == FakePlayer.this.getId()) {
                        connection.handleMovePlayer(new ServerboundMovePlayerPacket.Pos(movePacket.getXa(), movePacket.getYa(), movePacket.getZa(), isOnGround()));
                        //setDeltaMovement(new Vec3(movePacket.getXa(), movePacket.getYa(), movePacket.getZa()));
                    }
                }
            }
        };

        ServerConnectionListener listener = Utilities.getServer().getConnection();
        // Paper fix
        try {
            Field connectionsList = listener.getClass().getDeclaredField("g");
            connectionsList.setAccessible(true);
            List<Connection> connections = (List<Connection>) connectionsList.get(listener);
            connections.add(connectionSpoof);
        } catch (NoSuchFieldError ex) {
            ex.printStackTrace();
        }

        Utilities.getPlayerList().placeNewPlayer(connectionSpoof, this);
        getBukkitEntity().setMetadata("spoofed", new LazyMetadataValue(Spoofer.get(), () -> true));
    }

    public void runCommand(String command) {
        Spoofer.get().getLogger().info(name + " is running command " + command);
        getBukkitEntity().performCommand(command);
    }

    public void addChatListener(CommandSender sender) {
        senders.add(sender);
    }

    public void chat(String content) {
        getBukkitEntity().chat(content);
    }

    private String getPacketContent(ClientboundChatPacket packet) {
        try {
            Field adventureMessage = packet.getClass().getDeclaredField("adventure$message");
            adventureMessage.setAccessible(true);
            TextComponent component = (TextComponent) adventureMessage.get(packet);
            if (component != null) {
                WhyOhWhy why = new WhyOhWhy();
                ComponentFlattener.basic().flatten(component, why);
                return why.result.toString();
            }
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return packet.getMessage().getString();
    }

    private static class WhyOhWhy implements FlattenerListener {

        private final StringBuilder result = new StringBuilder();

        @Override
        public void component(@NotNull String text) {
            result.append(text);
        }
    }
}
