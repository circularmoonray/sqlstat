package com.github.circularmoonray.sqlstat;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class SqlStat extends JavaPlugin {
	private String host;
	private String port;
	private String db;
	private String id;
	private String pw;
	private Sql sql;
	private String url;


	@Override
	public void onEnable(){
		LoadConfig();
	}

	@Override
	public void onDisable(){
		getLogger().info("onDisableメソッドが呼び出されたよ！！");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String s = "";
		int stat = 0;
		boolean bool;
		UUID uuid = null;

		if (cmd.getName().equalsIgnoreCase("stat")) {

			if(args.length > 1){
				sender.sendMessage("引数は1つにまでにして下さい");
				return true;

			}else if(args.length == 1){
				//starのallオプション
				if(args[0].equalsIgnoreCase("all")){

					//オンラインプレイヤーのスコア取得
					for(Player player : this.getServer().getOnlinePlayers()){
						stat = getStat(player, Material.STONE);
						s = player.getDisplayName() + ":" + String.valueOf(stat);
						sender.sendMessage(s);
					}
					return true;
				}else{
					return false;
				}
			}

			if ((sender instanceof Player)) {
				stat = getStat(sender, Material.STONE);
				s = String.valueOf(stat);
				sender.sendMessage("あなたの石の掘削数:" + s);
			} else {
				sender.sendMessage("このコマンドはゲーム内から実行してください。");
			}
			return true;

		}else if(cmd.getName().equalsIgnoreCase("sql")){

			//オプションの判定
			if(args.length > 1){
				sender.sendMessage("引数は1つにまでにして下さい");
				return false;
			}else if(args.length == 1)
			{
				//configのリロードオプション
				if(args[0].equalsIgnoreCase("reload")){
					LoadConfig();
					NewSql();
					return true;
				}else if(args[0].equalsIgnoreCase("mine")){
					NewSql();
					for(Player player : this.getServer().getOnlinePlayers()){
						uuid = player.getUniqueId();
						s = player.getDisplayName();
						sql.insert("today", "name", s, uuid);
						stat = getStat(player, Material.STONE);
						sql.insert("today", "stone", stat, uuid);
						stat = getStat(player, Material.NETHERRACK);
						sql.insert("today", "netherrack", stat, uuid);
						stat = getStat(player, Material.GRAVEL);
						sql.insert("today", "gravel", stat, uuid);
					}
					sender.sendMessage("complete insert into 'today' of stats");
					return true;
				}else{
					return false;
				}
			}

			NewSql();
			if((sql.createTable("today", "", 0))){
				sender.sendMessage("create table");
			}else{
				sender.sendMessage("create table :" + Sql.exc);
			}

			sender.sendMessage("sql restart");

			return true;
		}

		return false;
	}

	//掘削数を表示
	private int getStat(CommandSender sender, Material material){
		Player player;
		int stat;
		player = (Player) sender;
		if(material.isBlock()){
			stat = (player).getStatistic(Statistic.MINE_BLOCK, material);
		}else{
			stat=0;
		}

		return stat;
	}

	//configの読み込み
	private boolean LoadConfig(){
		String s = "";
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		this.reloadConfig();
		host = this.getConfig().getString("host");
		port = this.getConfig().getString("port");
		db = this.getConfig().getString("db");
		id = this.getConfig().getString("id");
		pw = this.getConfig().getString("pw");

		s = host + port + db + id + pw;
		getLogger().info(s);

		if(port.equalsIgnoreCase("")){
			url = "jdbc:mysql://" + host;
		}else{
			url = "jdbc:mysql://" + host + ":" + port;
		}

		getLogger().info("config load completed");

		return true;
	}


	private boolean putStat(){

	}

	//sqlオブジェクトの作成
	private boolean NewSql(){
		//オブジェクトが存在したら切断処理
		if(sql != null){
			sql.disconnect();
			sql = null;
		}

		sql = new Sql(url, db, id, pw);

		if(sql == null){
			getLogger().info("fail NewSql");
			return false;
		}
		return true;
	}
}