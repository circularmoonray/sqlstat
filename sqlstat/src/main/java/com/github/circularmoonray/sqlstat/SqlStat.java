package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SqlStat extends JavaPlugin implements Listener {
	private String host;
	private String port;
	private String db;
	private String id;
	private String pw;
	private Sql sql;
	private String url;

	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		LoadConfig();
	}

	@Override
	public void onDisable(){
		getLogger().info("onDisableメソッドが呼び出されたよ！！");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		NewSql();
		putStat(today, player);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String s = "";
		int stat = 0;
		boolean bool;

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
				stat = ((Player) sender).getStatistic(Statistic.KILL_ENTITY, EntityType.ENDER_DRAGON);
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

			}else if(args.length == 1){
				//configのリロードオプション
				if(args[0].equalsIgnoreCase("reload")){
					LoadConfig();
					NewSql();
					return true;
				//現在ログイン中のプレイヤーの実績をSQLに書き出し
				}else if(args[0].equalsIgnoreCase("mine")){
					NewSql();
					for(Player player : this.getServer().getOnlinePlayers()){
						putStat(today, player);
					}
					sender.sendMessage("complete insert into 'today' of stats");
					return true;
				}else{
					return false;
				}
			}

			NewSql();
			if((sql.createTable(today, "", 0))){
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
	private int getStat(Player player, Material material){
		int stat;
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


	//掘削数をMySqlにプッシュ
	private boolean putStat(String str, Player player){
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