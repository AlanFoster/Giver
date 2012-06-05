package CommonCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import configs.MainConfig;
import configs.SpawnRecords;


import Spawners.EntityFactory;
import Spawners.MaterialFactory;
import Spawners.PetFactory;
import Spawners.SpawnerFactory;
import Spawners.Spawner;
import Utils.Helpers;

import Commands.CommandListener;

public class SpawnCommand extends CommandListener {
	/**
	 * Map of CommandName to the SpawnerObject
	 * IE, "cat" => PetSpawner(EntityType.OCELOT);
	 */
	private Map<String, Spawner> spawners;
	private List<SpawnerFactory> spawnFactories;

	private String validTypesHelpString;
	private boolean ignoreSpawnLimitsIfAdmin;
	
	
	public SpawnCommand(JavaPlugin plugin) {
		super("Spawner", "Spawns stuff. There is a limit per hour for each available type.", new String[] { "spawn", "s", "giver" }, 1, 1);
		spawners = new HashMap<String, Spawner>();

		spawnFactories = new LinkedList<SpawnerFactory>();
		createSpawnFactories();
		setupConfig(plugin.getConfig());

		//onCommandEvent(plugin.getServer().getWorld("world").getPlayers().get(0), "giver", new String[] { "cat" });
	}

	private void setupConfig(FileConfiguration config){
		config.options().copyDefaults(true);

		ignoreSpawnLimitsIfAdmin = config.getBoolean("ignoreSpawnLimitsIfAdmin");
		createAllSpawners(config.getConfigurationSection("spawnableTypes"));
		refreshValidTypes();
	}
		
	private void createSpawnFactories() {
		spawnFactories.add(new PetFactory());
		spawnFactories.add(new MaterialFactory());
		spawnFactories.add(new EntityFactory());
	}

	private void createAllSpawners(ConfigurationSection parent) {
		for (String commandName : parent.getKeys(false)) {
			boolean foundFactory = false;
			
			String entityName = parent.getString(commandName + ".entityName");
			Integer spawnLimit = parent.getInt(commandName + ".spawnLimitPerHour");

			// Test if the factories can create the required item
			for (SpawnerFactory spawnFactory : spawnFactories) {
				Spawner spawner = spawnFactory.createType(entityName, spawnLimit);

				if (spawner != null) {
					spawners.put(commandName.toLowerCase(), spawner);
					foundFactory = true;
					break;
				}
			}

			if (!foundFactory) {
				System.out.println("Error : No matching factory for entityName : " + entityName);
			}
		}
	}

	private void refreshValidTypes() {
		List<String> keys = new ArrayList<String>(spawners.keySet());
		Collections.sort(keys);
		validTypesHelpString = Helpers.join(keys.toArray());
	}

	
	@Override
	public boolean isValidArgs(Player player, String commandLabel, String[] args){
		if(!super.isValidArgs(player, commandLabel, args)){
			return false;
		}
		
		
		String spawnCommandName = args[0].toLowerCase();
		if(!spawners.containsKey(spawnCommandName)){
			tellUsage(player);			
			return false;
		}
		

		SpawnRecords spawnRecords = SpawnRecords.getInstance();
		Spawner spawner = spawners.get(spawnCommandName);
		
		int waitingTimeMinutes = spawnRecords.getTimeRemaining(player, spawner);
		if(waitingTimeMinutes > 0 && !(ignoreSpawnLimitsIfAdmin && player.isOp())) {
			player.sendMessage("Cannot create " + spawnCommandName + ". The limit is " + spawner.getSpawnLimit() + " per hour. Please wait " + waitingTimeMinutes + " minutes.");
			return false;
		}
		
		Location spawnLocation = getSpawnLocation(player);
		if (spawnLocation == null) {
			player.sendMessage("Unable to spawn " + spawnCommandName + " at location");
			return false;
		}
		
		return true;
	}
	
	@Override
	protected void execute(Player player, String type, String[] args) {
		String spawnCommandName = args[0].toLowerCase();
		SpawnRecords spawnRecords = SpawnRecords.getInstance();
		Spawner spawner = spawners.get(spawnCommandName);
		Location spawnLocation = getSpawnLocation(player);

		// Spawn and record the entity creation
		spawner.handleCreation(player, spawnLocation);
		spawnRecords.recordSpawn(player, spawner);
		player.sendMessage("Created new object :: " + spawnCommandName);
	}

	private Location getSpawnLocation(Player player) {
		Block lookedAtBlock = player.getTargetBlock(new HashSet<Byte>(), 4);
		Location spawnLocation =
				lookedAtBlock != null ? lookedAtBlock.getLocation() : player.getLocation();
		return spawnLocation;
	}


	@Override
	public String getDescription() {
		return super.getDescription() + "\n" + validTypesHelpString;
	}

	@Override
	public String getUsage() {
		return "Usage: /" + MainConfig.name + " <" + validTypesHelpString + ">";
	}
}
