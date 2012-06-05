package me.alanfoster.giver;

import me.alanfoster.utils.Helpers;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class CartListener implements Listener {
	// TODO see if there's already a way to test if it's valid for placing blocks
	// useInteractedBlock() and useItemInHand() seem to not be valid.
	// Consider using (event.getMaterial().toString().toLower().indexOf("minecart") > - 1) Maybe?
	private static final Material[] rails = {Material.POWERED_RAIL, Material.RAILS, Material.DETECTOR_RAIL};
	private static final Material[] minecarts = { Material.MINECART, Material.POWERED_MINECART, Material.STORAGE_MINECART};
	
    @EventHandler(priority = EventPriority.HIGHEST)   
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if(isValidMineCart(event)){ /* || isValidBoat(event) */
    		Player player = event.getPlayer();
    		
    		if(player.getGameMode() == GameMode.CREATIVE){
    			player.setItemInHand(null);
    		}
    	}
    }
    
    public boolean isValidMineCart(PlayerInteractEvent event){
    	return event.getAction() == Action.RIGHT_CLICK_BLOCK && Helpers.Contains(minecarts, event.getMaterial()) && Helpers.Contains(rails, event.getClickedBlock().getType());
    }
}