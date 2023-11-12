package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.craft.CraftSpoofedPlayer;
import io.netty.channel.*;
import io.netty.channel.local.LocalChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.*;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerRespawnEvent;
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
    private final CraftSpoofedPlayer player;
    private final SpoofedPlayer nmsPlayer;
    private final SocketAddress bind;

    public FakeConnection(CraftSpoofedPlayer player) throws UnknownHostException {
        super(PacketFlow.SERVERBOUND);
        this.player = player;
        this.nmsPlayer = player.getSpoofedPlayer();
        this.bind = new InetSocketAddress(InetAddress.getByName(NMSUtilities.getServer().getLocalIp()), 28000);
        this.channel = new LocalChannel();
        this.channel.unsafe().register(new DefaultEventLoop(), new DefaultChannelPromise(this.channel));
        this.channel.connect(new InetSocketAddress(InetAddress.getByName(NMSUtilities.getServer().getLocalIp()), NMSUtilities.getServer().getServerPort()), this.bind);
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext channelhandlercontext) throws Exception {
        super.channelActive(channelhandlercontext);
        super.channel = channelhandlercontext.channel();
        // super.channel.unsafe().register(new DefaultEventLoop(), new DefaultChannelPromise(super.channel));
    }

    @Override
    public void setListener(@NotNull PacketListener packetListener) {

        try {
            for (Field field : getClass().getSuperclass().getDeclaredFields()) {
                Spoofer.get().getLogger().info(field.getType().getName() + " " + field.getName());
            }
            Field listener = this.getClass().getSuperclass().getDeclaredField("p");
            listener.setAccessible(true);
            listener.set(this, packetListener);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
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
    public void send(Packet<?> packet, @Nullable PacketSendListener packetsendlistener) {
        if (IGNORED_PACKETS.contains(packet.getClass().getSimpleName())) return;
        if (packet instanceof ClientboundKeepAlivePacket) {
            nmsPlayer.connection.handleKeepAlive(new ServerboundKeepAlivePacket(((ClientboundKeepAlivePacket) packet).getId()));
        }

        if (nmsPlayer.isDeadOrDying() && !nmsPlayer.isRespawning()) {
            Spoofer.get().getLogger().info(player.getName() + " is dead, respawning in 20 ticks...");
            nmsPlayer.setRespawning(true);
            Bukkit.getScheduler().runTaskLater(Spoofer.get(), () -> {
                nmsPlayer.setRespawning(false);
                player.setHealth(20);
                NMSUtilities.getPlayerList().respawn(nmsPlayer, false, PlayerRespawnEvent.RespawnReason.DEATH);
            }, 20);
        }

        if (packet instanceof ClientboundPlayerChatPacket chatPacket) {
            //Spoofer.get().getLogger().info(String.valueOf(((ClientboundChatPacket) packet).getMessage()));
            player.getChatListeners().forEach(sender -> sender.sendMessage(Component.text(nmsPlayer.displayName).color(NamedTextColor.GRAY)
                    .append(Component.text(" > ").color(NamedTextColor.DARK_GRAY)).append(getPacketContent(chatPacket))));

        }
    }


    private Component getPacketContent(ClientboundPlayerChatPacket packet) {
        try {
            Field adventureMessage = packet.getClass().getDeclaredField("adventure$message");
            adventureMessage.setAccessible(true);
            TextComponent component = (TextComponent) adventureMessage.get(packet);
            if (component != null) return component;
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Component.text(packet.body().content());
    }
}
