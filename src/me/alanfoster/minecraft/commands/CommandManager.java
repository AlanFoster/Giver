package me.alanfoster.minecraft.commands;

import java.util.LinkedList;
import java.util.List;

import me.alanfoster.giver.commonCommands.SpawnCommand;
import me.alanfoster.giver.configs.GiverConfig;
import me.alanfoster.giver.configs.SpawnRecords;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandManager implements CommandExecutor {
	private JavaPlugin plugin;
	private List<ICommandEventListener> listeners;

	public CommandManager(JavaPlugin plugin) {
		this.plugin = plugin;
		listeners = new LinkedList<ICommandEventListener>();
	}

	public boolean registerListener(CommandListener newListener) {
		// Verify that there isn't already a listener that is listening for the
		// same label name
		for (ICommandEventListener existingListener : listeners) {
			for (String existingListenerLabel : existingListener.getHandles()) {
				for (String newListenerLabel : newListener.getHandles()) {
					if (existingListenerLabel.equals(newListenerLabel)) {
						System.out.println("The command :: "
										+ newListenerLabel
										+ " Is already registered within the command system");
						return false;
					}
				}
			}
		}

		listeners.add(newListener);
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command rawCommand, String rawAccesor, String[] rawArgs) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			for(ICommandEventListener commandListener : listeners){				
				if(commandListener.handlesCommand(rawAccesor)) {
					commandListener.onCommandEvent(player, rawAccesor, rawArgs);
					return true;
				}
			}
		}
		
		return false;
	}
}