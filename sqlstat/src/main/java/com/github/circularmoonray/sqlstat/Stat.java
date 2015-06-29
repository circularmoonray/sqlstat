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
		if(material.isBlock()){
			return ( (player).getStatistic(Statistic.MINE_BLOCK, material) );
		}

		return 0;
	}

	//掘削数をMySqlにプッシュ
	public boolean putMine(String str, Player player){
		UUID uuid = player.getUniqueId();
		int istat = 0;

		//プレイヤー名出力
		sql.insert(str, "name", player.getName(), uuid);

		//掘った数の出力
		istat = getStat(player, Material.STONE);
		sql.setCommands("stone", istat);
		istat = getStat(player, Material.SAND);
		sql.setCommands("sand", istat);
		istat = getStat(player, Material.NETHERRACK);
		sql.setCommands("netherrack", istat);
		istat = getStat(player, Material.DIRT);
		sql.setCommands("dirt", istat);
		istat = getStat(player, Material.GRAVEL);
		sql.setCommands("gravel", istat);
		istat = getStat(player, Material.LOG) + getStat(player, Material.LOG_2);
		sql.setCommands("logs", istat);
		istat = getStat(player, Material.OBSIDIAN);
		sql.setCommands("obsidian", istat);

		//死亡数出力
		istat = (player).getStatistic(Statistic.DEATHS);
		sql.setCommands("death", istat);

		//各出力データを送信
		return sql.insertCommands(str, uuid.toString());
	}

	public boolean putFarming(String str, Player player){
		UUID uuid = player.getUniqueId();

		//各データをセット : ダイピ・種・ジャガイモ・人参使用数
		sql.setCommands("use_dPickaxe",
				(player).getStatistic(Statistic.USE_ITEM, Material.DIAMOND_PICKAXE));
		sql.setCommands("use_seed",
				(player).getStatistic(Statistic.USE_ITEM, Material.SEEDS));
		sql.setCommands("use_potate",
				(player).getStatistic(Statistic.USE_ITEM, Material.POTATO_ITEM));
		sql.setCommands("use_carrot",
				(player).getStatistic(Statistic.USE_ITEM, Material.CARROT_ITEM));

		//各出力データを送信
		return sql.insertCommands(str, uuid.toString());
	}

	//getter & setter
}
