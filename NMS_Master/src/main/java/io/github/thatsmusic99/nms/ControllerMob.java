package io.github.thatsmusic99.nms;

import io.github.thatsmusic99.nms.pathfinder.WalkToLocationGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

/**
 * The Piglin Brute has the same size hitbox and also has the least amount of DataAccessors to add
 */
public class ControllerMob extends Mob {
    private static final EntityDataAccessor<Boolean> IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(ControllerMob.class, EntityDataSerializers.BOOLEAN);
    private org.bukkit.Location walkToLocation;
    private FakePlayer fakePlayer;

    protected ControllerMob(Level world) {
        super(EntityType.PIGLIN_BRUTE, world);
    }

    public static ControllerMob spawn (Location location, FakePlayer fakePlayer) {
        ServerLevel level = ((CraftWorld)location.getWorld()).getHandle();

        ControllerMob controller = new ControllerMob(level);
        controller.setPos(location.getX(), location.getY(), location.getZ());
        controller.setRot(location.getYaw(), location.getPitch());
        controller.setFakePlayer(fakePlayer);
        controller.setInvulnerable(true);
        controller.setInvisible(true);
        controller.setSilent(true);
        controller.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579);
        boolean value = VersionTranslator.addEntity(level, controller);
        return controller;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new WalkToLocationGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (fakePlayer == null) {
            kill();
            return;
        }

        Location location = getBukkitEntity().getLocation();
        fakePlayer.forceSetPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IMMUNE_TO_ZOMBIFICATION, true);
    }

    public Location getWalkToLocation() {
        return walkToLocation;
    }

    public void setWalkToLocation(Location walkToLocation) {
        this.walkToLocation = walkToLocation;
    }

    public FakePlayer getFakePlayer() {
        return fakePlayer;
    }

    public void setFakePlayer(FakePlayer fakePlayer) {
        this.fakePlayer = fakePlayer;
    }

    @Override
    public void push(double d0, double d1, double d2) {
    }

    @Override
    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return false;
    }

    /**
     * JUST DISABLE THE SOUNDS
     * GOTTA BE SNEAKY ABOUT IT ;)
     */
    @Override
    public void playAmbientSound() {}

    @Override
    protected void playStepSound(BlockPos blockposition, BlockState iblockdata) {}

    @Override
    protected void playSwimSound(float f) {}

    @Override
    protected void playEntityOnFireExtinguishedSound() {}

    /**
     * These methods prevent pets from being saved in the worlds
     */
    @Override
    public boolean saveAsPassenger(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public boolean save(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
    }

    /**
     * Prevents mob from teleporting from a portal
     */
    @Override
    public boolean isOnPortalCooldown() {
        return true;
    }

    @Override
    protected void handleNetherPortal() {}
}
