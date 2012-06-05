package me.alanfoster.giver.spawners;

import java.util.Map;

import me.alanfoster.giver.Permission;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class Spawner {
	protected String loggerName;
	private Map<Permission, Integer> spawnLimit;

	protected Spawner(String loggerName, Map<Permission, Integer> spawnLimit){
		this.loggerName = loggerName;
		this.spawnLimit = spawnLimit;
	}
	
	public abstract void handleCreation(Player player, Location location);
	
	public int getSpawnLimit(Permission permission){
		return spawnLimit.get(permission);
	}
	
	public String getLoggerName(){
		return loggerName;
	}
	
	@Override
	public String toString(){
		return "[Spawner for : " + loggerName + "; Spawn Limit : " + spawnLimit + "]";
	}
}