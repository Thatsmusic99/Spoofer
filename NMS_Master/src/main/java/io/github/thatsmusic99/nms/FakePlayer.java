package io.github.thatsmusic99.nms;

import io.github.thatsmusic99.abstraction.IFakePlayer;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;

public class FakePlayer extends ServerPlayer implements IFakePlayer {

    private final ServerLevel world;
    private final Connection connectionSpoof;
    private final SocketAddress bind;
    private final String name;
    private final HashSet<CommandSender> senders;
    private boolean respawning;
    private final ControllerMob controllerMob;

    public FakePlayer(Location origin, String name, Plugin plugin) throws NoSuchFieldException, IllegalAccessException, UnknownHostException {
        super(Utilities.getServer(), ((CraftWorld) origin.getWorld()).getHandle(), Utilities.determineProfile(name));
        this.name = name;
        this.respawning = false;
        this.senders = new HashSet<>();
        this.world = ((CraftWorld) origin.getWorld()).getHandle();
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
                if (packet instanceof ClientboundKeepAlivePacket) {
                    connection.handleKeepAlive(new ServerboundKeepAlivePacket(((ClientboundKeepAlivePacket) packet).getId()));
                }

                if (isDeadOrDying() && !respawning) {
                    respawning = true;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        respawning = false;
                        setHealth(20);
                        Utilities.getPlayerList().respawn(FakePlayer.this, false);
                    }, 20);
                }

                if (packet instanceof ClientboundChatPacket chatPacket) {
                    senders.forEach(sender -> sender.spigot().sendMessage(new ComponentBuilder(name).color(ChatColor.GRAY)
                            .append(" > ").color(ChatColor.DARK_GRAY).append(getPacketContent(chatPacket)).create()));

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
        setPos(origin.getX(), origin.getY(), origin.getZ());
        controllerMob = ControllerMob.spawn(origin, this);
        new BukkitRunnable() {
            @Override
            public void run() {
                teleportPlayer(origin);
            }
        }.runTaskLater(plugin, 2);
    }

    @Override
    public void runCommand(String command) {
        getBukkitEntity().performCommand(command);
    }

    @Override
    public void addChatListener(CommandSender sender) {
        senders.add(sender);
    }

    @Override
    public void chat(String content) {
        getBukkitEntity().chat(content);
    }

    @Override
    public void teleportPlayer(Location location) {
        teleportTo((ServerLevel) level, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        controllerMob.teleportTo(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void moveTo(Location location) {
        if (controllerMob == null) return;
        controllerMob.setWalkToLocation(location);
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

    @Override
    public boolean isOnPortalCooldown() {
        return true; // Prevents mob from teleporting from a portal
    }

    @Override
    protected void handleNetherPortal() {
        // fuck around and find out
    }
}
