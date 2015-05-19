package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LJoinQuit implements Listener{
	SqlStat plugin;

	LJoinQuit(SqlStat plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Stat stat = plugin.getStat();
		boolean statflag = plugin.getStatFlag();

		//プレイヤーの統計情報の書き出し
		Player player = event.getPlayer();
		stat.putMine(today, player);
		stat.putFarming(today, player);

		//集計コマンド稼働時、スコアボードへ出力
		if(statflag){

		}

	}

}
