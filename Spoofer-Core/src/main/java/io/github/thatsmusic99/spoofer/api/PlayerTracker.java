package io.github.thatsmusic99.spoofer.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class PlayerTracker {

    private final HashMap<String, IFakePlayer> fakePlayers;
    private static final PlayerTracker instance;

    static { instance = new PlayerTracker(); }

    public PlayerTracker() {
        fakePlayers = new HashMap<>();
    }

    public void addPlayer(String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> playerClass = Class.forName("io.github.thatsmusic99.spoofer.FakePlayer");
        Constructor<?> constructor = playerClass.getConstructor(String.class);
        fakePlayers.put(name, (IFakePlayer) constructor.newInstance(name));
    }

    public static PlayerTracker get() {
        return instance;
    }

    public IFakePlayer getPlayer(String name) {
        return fakePlayers.get(name);
    }
}
