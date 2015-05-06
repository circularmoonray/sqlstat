package com.github.circularmoonray.sqlstat;


import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stat {
	private Sql sql;

	Stat(String url, String  db, String  id, String  pw){
		sql = new Sql(url, db, id, pw);
	}

	//掘削数を表示
	public int getStat(CommandSender sender, Material material){
		return getStat((Player)sender, material);
	}

	public int getStat(Player player, Material material){
		int stat;
		if(material.isBlock()){
			stat = (player).getStatistic(Statistic.MINE_BLOCK, material);
		}else{
			stat=0;
		}

		return stat;
	}

	//掘削数をMySqlにプッシュ
	public boolean putMine(String str, Player player){
		UUID uuid = player.getUniqueId();
		String s = player.getDisplayName();
		int stat = 0;
		sql.insert(str, "name", s, uuid);
		stat = getStat(player, Material.STONE);
		sql.insert(str, "stone", stat, uuid);
		stat = getStat(player, Material.NETHERRACK);
		sql.insert(str, "netherrack", stat, uuid);
		stat = getStat(player, Material.DIRT);
		sql.insert(str, "dirt", stat, uuid);
		stat = getStat(player, Material.GRAVEL);
		sql.insert(str, "gravel", stat, uuid);

		stat = player.getStatistic(Statistic.BREAK_ITEM, Material.DIAMOND_PICKAXE);
		sql.insert(str, "break_dPickaxe", stat, uuid);
		stat = player.getStatistic(Statistic.USE_ITEM, Material.SEEDS);
		sql.insert(str, "use_seed", stat, uuid);

		stat = player.getStatistic(Statistic.DEATHS);
		sql.insert(str, "death", stat, uuid);

		return true;
	}

	//破棄時の処理
	protected void finalize(){
		//オブジェクトが存在したら切断処理
		if(sql != null){
			sql.disconnect();
			sql = null;
		}
	}
}
