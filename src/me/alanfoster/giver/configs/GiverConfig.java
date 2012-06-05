package me.alanfoster.giver.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.alanfoster.giver.Permission;

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
		
		FileConfiguration config = getConfig();
		
		refreshSpawnRequests();
		ignoreSpawnLimitsIfAdmin = config.getBoolean("ignoreSpawnLimitsIfAdmin");
	}
	
	private FileConfiguration getConfig(){
		FileConfiguration config = plugin.getConfig();
		
		if(!plugin.getDataFolder().exists()) {
			config.options().copyDefaults(true);
			plugin.saveConfig();
		}
		
		return config;
	}

	private List<SpawnRequest> loadSpawnRequests(){
		ConfigurationSection configRequests = getConfig().getConfigurationSection("spawnableTypes");
		
		List<SpawnRequest> spawnRequests = new ArrayList<SpawnRequest>();
		for (String commandName : configRequests.getKeys(false)) {
			String minecraftName = configRequests.getString(commandName + ".minecraftName");
			
			ConfigurationSection spawnLimitsRaw = configRequests.getConfigurationSection(commandName + ".spawnLimits");
			Map<Permission, Integer> spawnLimitsMap = new HashMap<Permission, Integer>();
			
			for(String permissionTypeString : spawnLimitsRaw.getKeys(false)){
				spawnLimitsMap.put(Permission.getMatchingPermission(permissionTypeString), configRequests.getInt(commandName + ".spawnLimits." + permissionTypeString));
			}
			
			if(minecraftName == null) {
				System.out.println("Invalid command :: " + commandName + ". Missing minecraftName in config.");
			} else {
				spawnRequests.add(new SpawnRequest(commandName, minecraftName, spawnLimitsMap));
			}
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
