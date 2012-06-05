package me.alanfoster.giver.spawners;

import java.util.Map;

import me.alanfoster.giver.Permission;

public abstract class SpawnerFactory {
	public abstract Spawner createType(String entityType, Map<Permission, Integer> spawnLimit);
}