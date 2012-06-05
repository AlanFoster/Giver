package me.alanfoster.commands;


import me.alanfoster.utils.Helpers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public abstract class CommandListener implements ICommandEventListener {
	protected final String[] handlingLabels;
	protected final String name;
	protected final String description;
	protected final int minArgs;
	protected final int maxArgs;
	
	protected CommandListener(String name, String description, String[] handlingLabels, int minArgs, int maxArgs){
		this.name = name;
		this.description = description;
		this.handlingLabels = handlingLabels;
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
	}

	public void onCommandEvent(Player player, String commandLabel, String[] args) {
		if(isValidArgs(player, commandLabel, args)) {
			execute(player, commandLabel, args);
		}
	}
	
	public boolean isValidArgs(Player player, String commandLabel, String[] args){
		int argSize = args.length;
		
		if(argSize < minArgs || argSize > argSize){
			tellUsage(player);			
			return false;
		}
		
		return true;
	}
	
	protected void tellUsage(Player player){
		player.sendMessage(ChatColor.RED + getUsage());			
	}
	
	protected abstract void execute(Player player, String commandLabel, String[] args);
	
	@Override
	public boolean handlesCommand(String label) {
		return Helpers.Contains(handlingLabels, label);
	}
	
	public String[] getHandles(){
		return handlingLabels;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
