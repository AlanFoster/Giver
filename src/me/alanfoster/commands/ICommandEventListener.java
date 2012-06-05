package me.alanfoster.commands;

import org.bukkit.entity.Player;

public interface ICommandEventListener extends ICommandOverview {
	public void onCommandEvent(Player player, String commandLabel, String[] args);
}
