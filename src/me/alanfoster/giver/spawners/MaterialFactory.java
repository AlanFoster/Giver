package me.alanfoster.giver.spawners;

import java.util.Map;

import me.alanfoster.giver.Permission;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MaterialFactory extends SpawnerFactory {
	@Override
	public Spawner createType(String entityName, Map<Permission, Integer> spawnLimit) {
		Material materialType = Material.matchMaterial(entityName);

		if (materialType != null) {
			return new MaterialSpawner(materialType, spawnLimit);
		}

		return null;
	}

	private static class MaterialSpawner extends Spawner {
		Material material;

		public MaterialSpawner(Material material, Map<Permission, Integer> spawnLimit) {
			super(material.toString(), spawnLimit);
			this.material = material;
		}

		@Override
		public void handleCreation(Player player, Location location) {
			player.setItemInHand(new ItemStack(material));

			player.sendMessage(material + " created");
		}
	}
}
