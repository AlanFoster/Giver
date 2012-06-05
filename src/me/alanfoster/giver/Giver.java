package me.alanfoster.giver;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.alanfoster.giver.commonCommands.SpawnCommand;
import me.alanfoster.giver.configs.GiverConfig;
import me.alanfoster.giver.configs.SpawnRecords;
import me.alanfoster.minecraft.commands.CommandManager;

import org.bukkit.plugin.java.JavaPlugin;

public class Giver extends JavaPlugin {
	private CommandManager commandManager;
	private GiverConfig giverConfig;
	private SpawnCommand spawnCommand;
	private static Logger logger;
	
	@Override
	public void onEnable(){
		logger = getLogger();
		
		logger.log(Level.ALL, ("We've now started up!"));	
		
		commandManager = new CommandManager(this);
		giverConfig = new GiverConfig(this);
		
		spawnCommand = new SpawnCommand(giverConfig);
		commandManager.registerListener(spawnCommand);
		
		
		getCommand("giver").setExecutor(commandManager);	
		
		// TODO Wrong
		SpawnRecords.setInstance(new SpawnRecords(this));
		
		getServer().getPluginManager().registerEvents(new CartListener(), this);
	}
		
	public static Logger getLogging(){
		return logger;
	}
	
	@Override
	public void onDisable(){
		System.out.println("Foo is now disabled");
	}
}