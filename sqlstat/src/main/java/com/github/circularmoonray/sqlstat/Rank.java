package com.github.circularmoonray.sqlstat;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class Rank {
	private final String name;
	private final List<String> permissions;

	public Rank(String name, List<String> permissions){
		this.name = name;
		this.permissions = permissions;
	}

	public String getName(Player player){
		return name.replaceAll("%name%", player.getName());
	}

	public void setPermissions(PermissionAttachment attachment, Player player){
		for(String perm : permissions){
			if(!player.hasPermission(perm)){
				attachment.setPermission(perm, true);
			}
		}
	}

	public String getName() {
		return name;
	}

	public List<String> getPermissions() {
		return permissions;
	}
}
