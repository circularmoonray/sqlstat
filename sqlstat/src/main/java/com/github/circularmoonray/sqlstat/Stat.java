package com.github.circularmoonray.sqlstat;


import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stat {
	private Sql sql;

	Stat(Config config){
		setSql(new Sql(config.getURL(),
				config.getDB(),
				config.getID(),
				config.getPW()));
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
		getSql().insert(str, "name", player.getName(), uuid);

		//掘った数の出力
		istat = getStat(player, Material.STONE);
		getSql().setCommands("stone", istat);
		istat = getStat(player, Material.SAND);
		getSql().setCommands("sand", istat);
		istat = getStat(player, Material.NETHERRACK);
		getSql().setCommands("netherrack", istat);
		istat = getStat(player, Material.DIRT);
		getSql().setCommands("dirt", istat);
		istat = getStat(player, Material.GRAVEL);
		getSql().setCommands("gravel", istat);
		istat = getStat(player, Material.LOG) + getStat(player, Material.LOG_2);
		getSql().setCommands("logs", istat);
		istat = getStat(player, Material.LOG) + getStat(player, Material.OBSIDIAN);
		getSql().setCommands("obsidian", istat);

		//死亡数出力
		istat = (player).getStatistic(Statistic.DEATHS);
		getSql().setCommands("death", istat);

		//各出力データを送信
		return getSql().insertCommands(str, uuid.toString());
	}

	public boolean putFarming(String str, Player player){
		UUID uuid = player.getUniqueId();

		//各データをセット : ダイピ・種・ジャガイモ・人参使用数
		getSql().setCommands("use_dPickaxe",
				(player).getStatistic(Statistic.USE_ITEM, Material.DIAMOND_PICKAXE));
		getSql().setCommands("use_seed",
				(player).getStatistic(Statistic.USE_ITEM, Material.SEEDS));
		getSql().setCommands("use_potate",
				(player).getStatistic(Statistic.USE_ITEM, Material.POTATO_ITEM));
		getSql().setCommands("use_carrot",
				(player).getStatistic(Statistic.USE_ITEM, Material.CARROT_ITEM));

		//各出力データを送信
		return getSql().insertCommands(str, uuid.toString());
	}

	//getter & setter

	public Sql getSql() {
		return sql;
	}

	public void setSql(Sql sql) {
		this.sql = sql;
	}
}
