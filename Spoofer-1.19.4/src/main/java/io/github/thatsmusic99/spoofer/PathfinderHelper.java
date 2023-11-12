package io.github.thatsmusic99.spoofer;

import io.github.thatsmusic99.spoofer.craft.CraftPathfinderHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.monster.Husk;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftMob;
import org.jetbrains.annotations.NotNull;

public class PathfinderHelper extends Husk {
    private final SpoofedPlayer npc;
    private final CraftPathfinderHelper helper;

    protected PathfinderHelper(SpoofedPlayer npc) {
        super(EntityType.HUSK, npc.level);
        setInvisible(true);
        setBaby(true);
        setSilent(true);
        setInvulnerable(true);

        this.npc = npc;
        this.helper = new CraftPathfinderHelper(this, npc.getCraftSpoofedPlayer());
        this.lookControl = new LookControlHelper(this);
        this.goalSelector.removeAllGoals(goal -> true);
        teleportTo(npc.getX(), npc.getY(), npc.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        npc.forceSetPositionRotation(getX(), getY(), getZ(), yHeadRot, getXRot());
        if (!isInvisible()) setInvisible(true);
    }

    @Override
    public @NotNull CraftEntity getBukkitEntity() {
        return helper;
    }

    @Override
    public @NotNull CraftMob getBukkitMob() {
        return helper;
    }

    public @NotNull CraftPathfinderHelper getBukkitHelper() {
        return helper;
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
