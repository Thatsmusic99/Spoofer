package io.github.thatsmusic99.spoofer;

import java.util.HashMap;

public class PlayerTracker {

    private final HashMap<String, FakePlayer> fakePlayers;
    private static final PlayerTracker instance;

    static { instance = new PlayerTracker(); }

    public PlayerTracker() {
        fakePlayers = new HashMap<>();
    }

    public void addPlayer(String name, FakePlayer player) {
        fakePlayers.put(name, player);
    }

    public static PlayerTracker get() {
        return instance;
    }

    public FakePlayer getPlayer(String name) {
        return fakePlayers.get(name);
    }
}
