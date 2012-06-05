package me.alanfoster.giver.configs;

import java.util.Map;

import me.alanfoster.giver.Permission;

public class SpawnRequest {
	private String commandTriggerName;
	private String minecraftName;
	private Map<Permission, Integer> spawnLimitPerHour;
	
	public SpawnRequest(String commandTriggerName, String minecraftName, Map<Permission, Integer> spawnLimitPerHour) {
		this.commandTriggerName = commandTriggerName;
		this.minecraftName = minecraftName;
		this.spawnLimitPerHour = spawnLimitPerHour;
	}
	
	public String getCommandTriggerName() {
		return commandTriggerName;
	}
	public String getMinecraftName() {
		return minecraftName;
	}
	
	public Map<Permission, Integer> getSpawnLimitPerHour() {
		return spawnLimitPerHour;
	}
}
