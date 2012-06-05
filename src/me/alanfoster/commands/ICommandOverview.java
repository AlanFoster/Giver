package me.alanfoster.commands;

import org.bukkit.entity.Player;

public interface ICommandOverview {
	public boolean handlesCommand(String label);
	public boolean isValidArgs(Player player, String commandLabel, String[] args);
	
	public String getName();
	public String getDescription();
	public String getUsage();
	public String[] getHandles();
}
