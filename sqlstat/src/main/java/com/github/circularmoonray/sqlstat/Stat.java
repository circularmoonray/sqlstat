package com.github.circularmoonray.sqlstat;


import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stat {
	private Sql sql;

	Stat(Config config){

		sql = new Sql(config.getURL(),
				config.getDB(),
				config.getID(),
				config.getPW());
	}

	//掘削数を表示
	public int getStat(CommandSender sender, Material material){
		return getStat((Player)sender, material);
	}

	public int getStat(Player player, Material material){
		int istat;
		if(material.isBlock()){
			istat = (player).getStatistic(Statistic.MINE_BLOCK, material);
		}else{
			istat=0;
		}

		return istat;
	}

	//掘削数をMySqlにプッシュ
	public boolean putMine(String str, Player player){
		UUID uuid = player.getUniqueId();
		String s = player.getDisplayName();
		int istat = 0;
		sql.insert(str, "name", s, uuid);
		istat = getStat(player, Material.STONE);
		sql.insert(str, "stone", istat, uuid);
		istat = getStat(player, Material.NETHERRACK);
		sql.insert(str, "netherrack", istat, uuid);
		istat = getStat(player, Material.DIRT);
		sql.insert(str, "dirt", istat, uuid);
		istat = getStat(player, Material.GRAVEL);
		sql.insert(str, "gravel", istat, uuid);

		istat = player.getStatistic(Statistic.BREAK_ITEM, Material.DIAMOND_PICKAXE);
		sql.insert(str, "break_dPickaxe", istat, uuid);
		istat = player.getStatistic(Statistic.USE_ITEM, Material.SEEDS);
		sql.insert(str, "use_seed", istat, uuid);

		istat = player.getStatistic(Statistic.DEATHS);
		sql.insert(str, "death", istat, uuid);

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
