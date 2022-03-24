package io.github.thatsmusic99.nms.pathfinder;

import io.github.thatsmusic99.nms.ControllerMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class WalkToLocationGoal extends Goal {
    private final ControllerMob controller;
    private final PathNavigation navigation;

    private int updateCountdownTicks;

    public WalkToLocationGoal(ControllerMob controller) {
        this.controller = controller;
        navigation = controller.getNavigation();

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (controller.getWalkToLocation() == null) return false;
        if (!(controller.getWalkToLocation().distance(controller.getBukkitEntity().getLocation()) > 0.2)) {
            controller.setWalkToLocation(null);
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = 10;
            Path path = fixThisStupidThing();
            alsoFixThis(path);
        }
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
    }

    @Override
    public void stop() {
        navigation.stop();
    }


    private Path fixThisStupidThing() {
        return navigation.createPath(
                new BlockPos (controller.getWalkToLocation().getX(), controller.getWalkToLocation().getY(), controller.getWalkToLocation().getZ()),
                1
        );
    }

    private void alsoFixThis(Path path) {
        navigation.moveTo(path, controller.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue());
    }
}
