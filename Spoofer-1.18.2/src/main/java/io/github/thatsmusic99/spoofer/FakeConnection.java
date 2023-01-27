package io.github.thatsmusic99.spoofer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
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
import net.minecraft.network.protocol.game.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;

public class FakeConnection extends Connection {

    private static final HashSet<String> IGNORED_PACKETS = new HashSet<>(Arrays.asList("PacketPlayOutSpawnEntityLiving",
            "ClientboundLevelChunkWithLightPacket"));
    private final FakePlayer player;
    private final SocketAddress bind;
    private Channel channel = super.channel;

    public FakeConnection(FakePlayer player) throws UnknownHostException {
        super(PacketFlow.CLIENTBOUND);
        this.player = player;
        this.bind = new InetSocketAddress(InetAddress.getByName(NMSUtilities.getServer().getLocalIp()), 28000);
    }

    @Override
    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
        super.channelActive(channelhandlercontext);
        this.channel = super.channel;
    }

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
        if (IGNORED_PACKETS.contains(packet.getClass().getSimpleName())) return;
        if (packet instanceof ClientboundKeepAlivePacket) {
            player.connection.handleKeepAlive(new ServerboundKeepAlivePacket(((ClientboundKeepAlivePacket) packet).getId()));
        }

        if (player.isDeadOrDying() && !player.isRespawning()) {
            Spoofer.get().getLogger().info(player.getName() + " is dead, respawning in 20 ticks...");
            player.setRespawning(true);
            Bukkit.getScheduler().runTaskLater(Spoofer.get(), () -> {
                player.setRespawning(false);
                player.setHealth(20);
                NMSUtilities.getPlayerList().respawn(player, false);
            }, 20);
        }

        if (packet instanceof ClientboundChatPacket chatPacket) {
            //Spoofer.get().getLogger().info(String.valueOf(((ClientboundChatPacket) packet).getMessage()));
            player.getSenders().forEach(sender -> sender.spigot().sendMessage(new ComponentBuilder(player.displayName).color(ChatColor.GRAY)
                    .append(" > ").color(ChatColor.DARK_GRAY).append(getPacketContent(chatPacket)).create()));

        }

        if (packet instanceof ClientboundSetEntityMotionPacket movePacket) {
            if (movePacket.getId() == player.getId()) {
                player.connection.handleMovePlayer(new ServerboundMovePlayerPacket.Pos(movePacket.getXa(), movePacket.getYa(), movePacket.getZa(), player.isOnGround()));
                //setDeltaMovement(new Vec3(movePacket.getXa(), movePacket.getYa(), movePacket.getZa()));
            }
        }
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
