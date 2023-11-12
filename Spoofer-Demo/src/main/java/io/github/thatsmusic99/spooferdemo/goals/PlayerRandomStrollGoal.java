package io.github.thatsmusic99.spooferdemo.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import io.github.thatsmusic99.spoofer.api.ISpoofedPlayer;
import io.github.thatsmusic99.spooferdemo.SpooferDemo;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class PlayerRandomStrollGoal implements Goal<ISpoofedPlayer> {

    private static final GoalKey<ISpoofedPlayer> key;
    private final ISpoofedPlayer player;

    static {
        key = GoalKey.of(ISpoofedPlayer.class, NamespacedKey.fromString("random_stroll", SpooferDemo.get()));
    }

    public PlayerRandomStrollGoal(ISpoofedPlayer player) {
        this.player = player;
    }

    @Override
    public void tick() {

        //
    }

    @Override
    public boolean shouldActivate() {
        return true;
    }

    @Override
    public @NotNull GoalKey<ISpoofedPlayer> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.JUMP);
    }
}
