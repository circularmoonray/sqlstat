package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LJoinQuit implements Listener {

	private SqlStat plugin;

	public LJoinQuit(SqlStat plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){

		//集計フラグがオンだった場合、石の集計
		if(plugin.fCount){
			Count count = plugin.count;
			Player player = event.getPlayer();

			count.setSmine(player, (player).getStatistic(Statistic.MINE_BLOCK, Material.STONE));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Stat stat = plugin.getStat();

		stat.putMine(today, player);
		stat.putFarming(today, player);

		//集計フラグがオンだった場合、石の集計
		if(plugin.fCount){
			Count count = plugin.count;

			count.setEmine(player, (player).getStatistic(Statistic.MINE_BLOCK, Material.STONE));
		}
	}

}
