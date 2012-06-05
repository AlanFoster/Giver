package Spawners;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;

import Utils.Helpers;

public class PetFactory extends EntityFactory {
	// This might be stored somewhere already?
	private final static EntityType[] pets = { EntityType.OCELOT, EntityType.WOLF };

	@Override
	public Spawner createType(String entityName, int spawnLimit) {
		EntityType entityType = getEntityTypeFromName(entityName);

		if (entityType != null && Helpers.Contains(pets, entityType)) {
			return new PetSpawner(entityType, spawnLimit);
		}

		return null;
	}

	protected static class PetSpawner extends Spawner {
		EntityType entityType;
		
		public PetSpawner(EntityType entityType, int spawnLimit) {
			super(entityType.toString(), spawnLimit);
			this.entityType = entityType;
		}

		@Override
		public void handleCreation(Player player, Location location) {
			Tameable pet = (Tameable) player.getWorld().spawnCreature(location, entityType);
			((Ageable) pet).setBaby();
			
			if(entityType == EntityType.OCELOT){
				((Ocelot) pet).setCatType(getRandomCatBreed());
			}
			
			pet.setOwner(player);
			player.sendMessage("New baby " + entityType + " tamed!");
		}
		
		public Type getRandomCatBreed(){
			Type[] possibleTypes =  new Type[]{Type.BLACK_CAT, Type.RED_CAT, Type.SIAMESE_CAT};
			return possibleTypes[(int) (Math.random() * possibleTypes.length)];
		}
	}
}
