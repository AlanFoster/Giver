package me.alanfoster.giver;

import java.util.Arrays;
import java.util.logging.Logger;

import me.alanfoster.giver.commonCommands.SpawnCommand;
import me.alanfoster.giver.configs.GiverConfig;
import me.alanfoster.giver.configs.SpawnRecords;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Giver extends JavaPlugin implements Listener {
	private GiverConfig giverConfig;
	private SpawnCommand spawnCommand;
	
	@Override
	public void onEnable(){
		giverConfig = new GiverConfig(this);
		
		spawnCommand = new SpawnCommand(giverConfig);
		
		// TODO Wrong
		SpawnRecords.setInstance(new SpawnRecords(this));
		
		getServer().getPluginManager().registerEvents(new CartListener(), this);
		
		this.getServer().getPluginManager().registerEvents(this, this);
	}
		
	@Override
	public void onDisable(){
		System.out.println("Foo is now disabled");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		String message = event.getMessage().toLowerCase();
		
		if(message.startsWith("/" + GiverConfig.getListenCommand())) {
			String[] messageSplit = message.split(" ");
			String[] args = messageSplit.length > 0 ? Arrays.copyOfRange(messageSplit, 1, messageSplit.length) : new String[0];
			
			spawnCommand.onCommandEvent(event.getPlayer(), GiverConfig.getListenCommand(), args);
			event.setCancelled(true);
		}
	}
}