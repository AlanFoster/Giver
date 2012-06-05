package Commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import configs.SpawnRecords;



import CommonCommands.HelpMenuCommand;
import CommonCommands.ReportCommand;
import CommonCommands.SpawnCommand;

public final class CommandManager implements CommandExecutor {
	private JavaPlugin plugin;
	
	private List<ICommandEventListener> listeners;

	public CommandManager(JavaPlugin plugin) {
		this.plugin = plugin;
		
		// TODO Wrong
		SpawnRecords.setInstance(new SpawnRecords(plugin));
		
		listeners = new LinkedList<ICommandEventListener>();
		registerCommonListeners();
	}

	private void registerCommonListeners() {
		registerListener(new SpawnCommand(plugin));
		// registerListener(new ReportCommand());
		// registerListener(new HelpMenuCommand(listeners));
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
			
			String actualCommand = rawArgs.length > 0 ? rawArgs[0] : "";
			String[] args = rawArgs.length > 1 ? Arrays.copyOfRange(rawArgs, 1, rawArgs.length) : new String[0];
			
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