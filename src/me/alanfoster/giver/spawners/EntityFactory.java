package me.alanfoster.giver.spawners;

import java.util.Map;

import me.alanfoster.giver.Permission;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class EntityFactory extends SpawnerFactory {
	@Override
	public Spawner createType(String entityName, Map<Permission, Integer> spawnLimit) {
		EntityType entityType = getEntityTypeFromName(entityName);
		if(entityType != null){
			return new MiscSpawner(entityType, spawnLimit);
		}

		return null;
	}

	protected EntityType getEntityTypeFromName(String name){
		for(EntityType entityType : EntityType.values()){
			// entityType.getName() seems to return null on some types...
			if(name.equalsIgnoreCase(entityType.getName())){
				return entityType;
			}
		}
		return null;
	}
	
	private static class MiscSpawner extends Spawner {	
		EntityType entityType;
		
		public MiscSpawner(EntityType entityType, Map<Permission, Integer> spawnLimit) {
			super(entityType.toString(), spawnLimit);
			this.entityType = entityType;
		}
		
		@Override
		public void handleCreation(Player player, Location location){
			player.getWorld().spawn(player.getLocation(), entityType.getEntityClass());
			
			player.sendMessage(entityType + " created");
		}
	}
}

