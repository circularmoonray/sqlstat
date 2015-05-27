package com.github.circularmoonray.sqlstat;

import org.bukkit.entity.Player;

public class Utilities {

	public static void sendOPMessage(String str){
		SqlStat plugin = SqlStat.instance;

		for ( Player player : plugin.getServer().getOnlinePlayers() ) {
			if ( player.isOp()) {
				player.sendMessage(str);
			}
		}
	}

	public static void sendEveryMessage(String str){
		SqlStat plugin = SqlStat.instance;

		for ( Player player : plugin.getServer().getOnlinePlayers() ) {
			player.sendMessage(str);
		}
	}
}
