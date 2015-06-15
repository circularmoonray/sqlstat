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

		//ランク判定
		calcRankBorder(event);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
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


	private void calcRankBorder(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Config config = plugin.getconfig();
		Integer level = 0;
		String name = player.getDisplayName();

		//係数有りパラメータ
		level += (int)((player).getStatistic(Statistic.MINE_BLOCK, Material.STONE) *
				config.getResultset().get("stone"));
		level += (int)((player).getStatistic(Statistic.MINE_BLOCK, Material.NETHERRACK) *
				config.getResultset().get("netherrack"));
		level +=(int)((player).getStatistic(Statistic.MINE_BLOCK, Material.DIRT) *
				config.getResultset().get("dirt"));
		level += (int)((player).getStatistic(Statistic.MINE_BLOCK, Material.GRAVEL) *
				config.getResultset().get("gravel"));
		level += (int)(((player).getStatistic(Statistic.MINE_BLOCK, Material.LOG) +
				(player).getStatistic(Statistic.MINE_BLOCK, Material.LOG_2)) *
				config.getResultset().get("log"));
		level += (int)((player).getStatistic(Statistic.USE_ITEM, Material.SEEDS) *
				config.getResultset().get("seed"));
		level += (int)((player).getStatistic(Statistic.USE_ITEM, Material.POTATO_ITEM) *
				config.getResultset().get("potate"));
		level += (int)((player).getStatistic(Statistic.USE_ITEM, Material.CARROT_ITEM) *
				config.getResultset().get("carrot"));

		//別途1倍パラメータ
		level += (int)((player).getStatistic(Statistic.MINE_BLOCK, Material.ENDER_STONE));

		//比較対象はツリーマップなので、最小値から順に比べ、整地力に応じて設定する
		for(Integer i : config.getRankset().keySet()){
			if(level >= i){
				//プレイヤーネームの設定
				name = config.getRankset().get(i).getName(player);
				//権限の付与
				config.getRankset().get(i).setPermissions(player.addAttachment(plugin), player);
			}else{
				break;
			}
		}

		player.setDisplayName(name);
	}

}
