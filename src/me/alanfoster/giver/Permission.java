package me.alanfoster.giver;

import org.bukkit.entity.Player;

public enum Permission {
	NORMAL("giver.normal"),
	SUPER("giver.super");
	
	private String permissionValue;
	
	private Permission(String permissionValue) {
		this.permissionValue = permissionValue;
	}
	
	public String getPermissionValue(){
		return permissionValue;
	}

	public static Permission getBestPermission(Player player) {
		Permission bestPermission = null;
		for(Permission permission : Permission.values()){
			if(player.hasPermission(permission.getPermissionValue())) {
				bestPermission = permission;
			}
		}
		return bestPermission;
	}

	public static Permission getMatchingPermission(String permissionType) {
		for(Permission permission : Permission.values()){
			if(permissionType.equalsIgnoreCase(permission.toString())) {
				return permission;
			}
		}
		return null;
	}
}
