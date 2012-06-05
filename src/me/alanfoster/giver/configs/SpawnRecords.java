package me.alanfoster.giver.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.crypto.dsig.keyinfo.PGPData;

import me.alanfoster.giver.Permission;
import me.alanfoster.giver.spawners.Spawner;
import me.alanfoster.giver.spawners.SpawnerFactory;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class SpawnRecords {
	private static final String saveLocation = "spawnRecords.yml";
	private static SpawnRecords instance;
	
	
	private FileConfiguration spawnRecords;
	private File spawnRecordsFile;
	
	private JavaPlugin plugin;
	
	public SpawnRecords(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	public static void setInstance(SpawnRecords instance){
		SpawnRecords.instance = instance;
	}

	public static SpawnRecords getInstance(){
		if(instance == null){
			throw new NullPointerException("SpawnRecords instance not set");
		}
		return instance;
	}
	
	private void reloadSpawnRecords(){
		if(spawnRecordsFile == null){
			spawnRecordsFile = new File(plugin.getDataFolder(), saveLocation);
		}

		spawnRecords = YamlConfiguration.loadConfiguration(spawnRecordsFile);
		
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(saveLocation);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        spawnRecords.setDefaults(defConfig);
	    }
	}
	
	private FileConfiguration getSpawnRecords(){
		if(spawnRecords == null){
			reloadSpawnRecords();
		}
		return spawnRecords;
	}

	// TODO rewrite
	public int getTimeRemaining(Player player, Spawner spawner){
		String playerName = player.getName();
		FileConfiguration spawnRecords = getSpawnRecords();
			
		if(spawnRecords.contains(playerName)){
			List<Long> previousSpawnTimes = spawnRecords.getLongList(playerName + "." + spawner.getLoggerName());
			
			if(previousSpawnTimes != null){
				Permission playerPermission = Permission.getBestPermission(player);
				
				long previousHourMilliseconds = System.currentTimeMillis() - 3600000;
				
				int pastHourSpawnCount = 0;
				long latestSpawnTimeMilliseconds = 0;
				
				Iterator<Long> iterator = previousSpawnTimes.iterator();
				while(iterator.hasNext()){
					latestSpawnTimeMilliseconds = iterator.next();
					if(latestSpawnTimeMilliseconds > previousHourMilliseconds){
						pastHourSpawnCount++;
							
						if(pastHourSpawnCount == spawner.getSpawnLimit(playerPermission)){
							return new Date(latestSpawnTimeMilliseconds - System.currentTimeMillis()).getMinutes();
						}
					}
				}
			}
		}

		return 0;
	}

	public void recordSpawn(Player player, Spawner spawner){
		FileConfiguration spawnRecords = getSpawnRecords();
		String playerName = player.getName();
		String loggerName = spawner.getLoggerName();
		
		List<Long> spawnTimes = spawnRecords.getLongList(playerName + "." + loggerName);
		if(spawnTimes == null){
			spawnTimes = new ArrayList<Long>();
		} 
		spawnTimes.add(0, Calendar.getInstance().getTimeInMillis());

		spawnRecords.set(playerName + "." + loggerName, spawnTimes);
		
		try {
			spawnRecords.save(spawnRecordsFile);
		} catch(IOException e){
			System.out.println("Failed to save spawnRecords for " + player + " for spawner : " + spawner);
		}
	}
}
