package me.alanfoster.giver.configs;

public class SpawnRequest {
	private String commandTriggerName;
	private String minecraftName;
	private int spawnLimitPerHour;
	
	public SpawnRequest(String commandTriggerName, String minecraftName,
			int spawnLimitPerHour) {
		super();
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
	public int getSpawnLimitPerHour() {
		return spawnLimitPerHour;
	}
}
