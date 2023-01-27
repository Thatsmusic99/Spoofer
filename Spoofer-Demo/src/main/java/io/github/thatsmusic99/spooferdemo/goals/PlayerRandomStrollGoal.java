package io.github.thatsmusic99.spooferdemo.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import io.github.thatsmusic99.spooferdemo.SpooferDemo;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creature;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;

public class PlayerRandomStrollGoal implements Goal<Creature> {

    private static final GoalKey<Creature> key;

    static {
        key = GoalKey.of(Creature.class, NamespacedKey.fromString("random_stroll", SpooferDemo.get()));
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
    public @NotNull GoalKey<Creature> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.JUMP);
    }
}
