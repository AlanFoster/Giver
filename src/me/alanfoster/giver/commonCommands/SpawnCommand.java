package me.alanfoster.giver.commonCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.alanfoster.giver.Permission;
import me.alanfoster.giver.configs.GiverConfig;
import me.alanfoster.giver.configs.SpawnRecords;
import me.alanfoster.giver.configs.SpawnRequest;
import me.alanfoster.giver.spawners.EntityFactory;
import me.alanfoster.giver.spawners.MaterialFactory;
import me.alanfoster.giver.spawners.PetFactory;
import me.alanfoster.giver.spawners.Spawner;
import me.alanfoster.giver.spawners.SpawnerFactory;
import me.alanfoster.minecraft.commands.CommandListener;
import me.alanfoster.utils.Helpers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SpawnCommand extends CommandListener {
	/**
	 * Map of CommandName to the SpawnerObject
	 * IE, "cat" => PetSpawner(EntityType.OCELOT, Integer.max);
	 */
	private Map<String, Spawner> spawners;
	private List<SpawnerFactory> spawnFactories;

	private String validTypesHelpString;
	private boolean ignoreSpawnLimitsIfAdmin;
	
	public SpawnCommand(GiverConfig config) {
		super("Spawner", "Spawns stuff. There is a limit per hour for each available type.", new String[] { "spawn", "s", "giver" }, 1, 1);
	
		createSpawnFactories();
		createAllSpawners(config.getSpawnRequests());
		ignoreSpawnLimitsIfAdmin = config.getIgnoreSpawnLimitsIfAdmin();
	
		refreshValidTypes();
		//onCommandEvent(plugin.getServer().getWorld("world").getPlayers().get(0), "giver", new String[] { "cat" });
	}

	private void createSpawnFactories() {
		spawnFactories = new LinkedList<SpawnerFactory>();
		spawnFactories.add(new PetFactory());
		spawnFactories.add(new MaterialFactory());
		spawnFactories.add(new EntityFactory());
	}
	
	private void createAllSpawners(List<SpawnRequest> spawnRequests) {
		spawners = new HashMap<String, Spawner>();
		for (SpawnRequest spawnRequest : spawnRequests) {
			boolean createdSpawner = createSpawner(spawnRequest);
			
			if(!createdSpawner) {
				System.out.println("Error : No matching factory for minecraftname : " + spawnRequest.getMinecraftName());
			}
		}
	}
	
	private boolean createSpawner(SpawnRequest spawnRequest) {
		// Test if the factories can create the required item
		for (SpawnerFactory spawnFactory : spawnFactories) {
			Spawner spawner = spawnFactory.createType(spawnRequest.getMinecraftName(), spawnRequest.getSpawnLimitPerHour());

			if (spawner != null) {
				spawners.put(spawnRequest.getCommandTriggerName(), spawner);
				return true;
			}
		}
		return false;
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
		
		
		Permission playerPermission = Permission.getBestPermission(player);
		int waitingTimeMinutes = spawnRecords.getTimeRemaining(player, spawner);
		if(waitingTimeMinutes > 0 && !(ignoreSpawnLimitsIfAdmin && player.isOp())) {
			player.sendMessage("Cannot create " + spawnCommandName + ". The limit for a " + playerPermission + " user is " + spawner.getSpawnLimit(playerPermission) + " per hour. Please wait " + waitingTimeMinutes + " minutes.");
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
		return super.getDescription() + "\n" + getValidTypesHelpString();
	}

	@Override
	public String getUsage() {
		return "Usage: /" + GiverConfig.getListenCommand() + " <" + getValidTypesHelpString() + ">";
	}
	
	private String getValidTypesHelpString(){
		if(validTypesHelpString == null){
			refreshValidTypes();
		}
		
		return validTypesHelpString;
	}
}
