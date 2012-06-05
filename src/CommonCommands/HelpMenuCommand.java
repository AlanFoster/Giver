package CommonCommands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import configs.MainConfig;

import Utils.Helpers;


import Commands.CommandListener;
import Commands.ICommandEventListener;
import Commands.ICommandOverview;

public class HelpMenuCommand extends CommandListener {
	private String helpMessage;
	
	public HelpMenuCommand(List<ICommandEventListener> descriptions){
		super("Help", "Gives an overview of what to do", new String[]{"", "?", "help"}, -1, Integer.MAX_VALUE);
		
		helpMessage = createMessage(descriptions);
	}
	
	private String createMessage(List<ICommandEventListener> descriptions){
		int i = 1;
		String space = "     ";
		
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.BLUE).append("The tools offered by foo are ::\n").append(ChatColor.WHITE);

		for(ICommandOverview description : descriptions){
			sb.append(ChatColor.GREEN).append(i).append(". ").append(ChatColor.WHITE);
			sb.append("Name : ").append(description.getName()).append("\n");
			sb.append(space).append("Description : ").append(description.getDescription().replace("\n", "\n" + space)).append("\n");
			sb.append(space).append("Usage : ").append(description.getUsage()).append("\n");
			sb.append("\n");
			i++;
		}
		
		return sb.toString();
	}

	@Override
	protected void execute(Player player, String type, String[] args) {
		player.sendMessage(helpMessage);
	}

	@Override
	public String getUsage() {
		return "Usage: /" + MainConfig.name + " help";
	}
}
