package io.github.thatsmusic99.spoofer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.LookControl;

public class PathfinderHelper extends PathfinderMob {
    private final FakePlayer npc;
    private boolean pendingTeleport;

    protected PathfinderHelper(FakePlayer npc) {
        super(EntityType.ZOMBIE, npc.level);
        // setInvisible(true);
        setBaby(true);
        setSilent(true);

        this.pendingTeleport = false;
        this.npc = npc;
        this.lookControl = new LookControlHelper(this);
        this.goalSelector.removeAllGoals();
        teleportTo(npc.getX(), npc.getY(), npc.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        if (pendingTeleport) {
            level = npc.level;
            teleportTo(npc.getX(), npc.getY(), npc.getZ());
            pendingTeleport = false;
        } else {
            npc.forceSetPositionRotation(getX(), getY(), getZ(), yHeadRot, getXRot());
        }

        // if (!isInvisible()) setInvisible(true);
    }

    public void pendTeleport() {
        pendingTeleport = true;
    }

    private class LookControlHelper extends LookControl {

        public LookControlHelper(Mob entity) {
            super(entity);
        }

        @Override
        public void tick() {
            super.tick();
            npc.yHeadRot = yHeadRot;
            npc.setXRot(getXRot());
        }
    }
}
