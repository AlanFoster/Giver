package main;

import org.bukkit.plugin.java.JavaPlugin;

import Commands.CommandManager;

public class Giver extends JavaPlugin {
	private CommandManager commandManager;

	@Override
	public void onEnable(){
		System.out.println("We've now started up!");	
		
		commandManager = new CommandManager(this);

		getCommand("giver").setExecutor(commandManager);	
		
		getServer().getPluginManager().registerEvents(new CartListener(), this);
	}
	
	@Override
	public void onDisable(){
		System.out.println("Foo is now disabled");
	}
}