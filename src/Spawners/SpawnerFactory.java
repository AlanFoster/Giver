package Spawners;

import org.bukkit.entity.EntityType;

public abstract class SpawnerFactory {
	public abstract Spawner createType(String entityType, int spawnLimit);
}