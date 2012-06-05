package Spawners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class Spawner {
	protected String loggerName;
	
	private int spawnLimit;

	protected Spawner(String loggerName, int spawnLimit){
		this.loggerName = loggerName;
		this.spawnLimit = spawnLimit;
	}
	
	public abstract void handleCreation(Player player, Location location);
	
	public int getSpawnLimit(){
		return spawnLimit;
	}
	
	public String getLoggerName(){
		return loggerName;
	}
	
	@Override
	public String toString(){
		return "[Spawner for : " + loggerName + "; Spawn Limit : " + spawnLimit + "]";
	}
}