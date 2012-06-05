package me.alanfoster.giver.configs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GiverConfig {
	public static final String name = "giver";
	private JavaPlugin plugin;
	private List<SpawnRequest> cachedSpawnRequests;
	private boolean ignoreSpawnLimitsIfAdmin;
	
	public GiverConfig(JavaPlugin plugin){
		this.plugin = plugin;
		
		FileConfiguration config = plugin.getConfig();
		
		config.options().copyDefaults(true);
		
		refreshSpawnRequests();
		ignoreSpawnLimitsIfAdmin = config.getBoolean("ignoreSpawnLimitsIfAdmin");
	}

	private List<SpawnRequest> loadSpawnRequests(){
		ConfigurationSection configRequests = plugin.getConfig().getConfigurationSection("spawnableTypes");
		
		List<SpawnRequest> spawnRequests = new ArrayList<SpawnRequest>();
		for (String commandName : configRequests.getKeys(false)) {
			String minecraftName = configRequests.getString(commandName + ".entityName");
			Integer spawnLimit = configRequests.getInt(commandName + ".spawnLimitPerHour");
			spawnRequests.add(new SpawnRequest(commandName, minecraftName, spawnLimit));
		}
		
		return spawnRequests;
	}
	
	public void refreshSpawnRequests(){
		cachedSpawnRequests = loadSpawnRequests();
	}
	
	public List<SpawnRequest> getSpawnRequests(){
		if(cachedSpawnRequests == null){
			refreshSpawnRequests();
		}
		return cachedSpawnRequests;
	}

	public boolean getIgnoreSpawnLimitsIfAdmin() {
		return ignoreSpawnLimitsIfAdmin;
	}
	
	
}
