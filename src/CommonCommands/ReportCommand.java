package CommonCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import configs.MainConfig;

import Utils.Helpers;

import Commands.CommandListener;

public class ReportCommand extends CommandListener {

	public ReportCommand() {
		super("report", "Reports a user to admins", new String[]{"report"}, 2, 2);
	}
	
	@Override
	public boolean isValidArgs(Player player, String commandLabel, String[] args){
		if(!super.isValidArgs(player, commandLabel, args)){
			return false;
		}
		
		String reportName = args[0];
		
		if(player.getName().equals(reportName)) {
			player.sendMessage("You cannot report yourself");
			return false;
		}
		
		Player reportedPlayer = Bukkit.getServer().getPlayer(reportName);
		
		if(reportedPlayer == null){
			player.sendMessage("The player " + reportName + " does not exist.");
			return false;
		}
		
		if(reportedPlayer.isOp()){
			player.sendMessage("You cannot report an op");
			return false;
		}
		
		return true;
	}

	@Override
	protected void execute(Player player, String commandLabel, String[] args) {
		player.sendMessage("Yo ho ho. " + args[0] + " was reported!");
	}

	@Override
	public String getUsage() {
		return "Usage: /" + MainConfig.name + " report <player> <reason>";
	}
}
