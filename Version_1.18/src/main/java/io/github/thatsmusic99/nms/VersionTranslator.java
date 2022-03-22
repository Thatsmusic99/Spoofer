package io.github.thatsmusic99.nms;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class VersionTranslator {
    public static boolean addEntity (Level level, Entity entity) {
        return level.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
}
