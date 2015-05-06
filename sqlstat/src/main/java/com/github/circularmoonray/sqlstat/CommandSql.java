package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class CommandSql implements Listener {
	SqlStat plugin;

	public CommandSql(Plugin plugin){
		this.plugin = plugin;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String s = "";
		int istat = 0;
		boolean bool;

		if (cmd.getName().equalsIgnoreCase("stat")) {

			if(args.length > 1){
				sender.sendMessage("引数は1つにまでにして下さい");
				return true;

			}else if(args.length == 1){
				//starのallオプション
				if(args[0].equalsIgnoreCase("all")){

					//オンラインプレイヤーのスコア取得
					for(Player player : plugin.getServer().getOnlinePlayers()){
						istat = plugin.stat.getStat(player, Material.STONE);
						s = player.getDisplayName() + ":" + String.valueOf(istat);
						sender.sendMessage(s);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("set")){

					((Player) sender).setStatistic(Statistic.KILL_ENTITY, EntityType.ENDERMAN, 10);
					return true;
				}else{
					return false;
				}
			}

			if ((sender instanceof Player)) {
				istat = ((Player) sender).getStatistic(Statistic.KILL_ENTITY, EntityType.ENDER_DRAGON);
				s = String.valueOf(istat);
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

					sender.sendMessage("reload completed");
					return true;

				}else if(args[0].equalsIgnoreCase("mine")){
					//現在ログイン中のプレイヤーの実績をSQLに書き出し

					for(Player player : plugin.getServer().getOnlinePlayers()){
						stat.putMine(today, player);
					}
					sender.sendMessage("complete insert and update into 'today' of stats");
					return true;
				}else{
					return false;
				}
			}

			sender.sendMessage("sql restart");

			return true;
		}

		return false;
	}

	//configの読み込み
		private boolean LoadConfig(){
			String host;
			String port;

			String s = "";
			plugin.getConfig().options().copyDefaults(true);
			plugin.saveDefaultConfig();
			plugin.reloadConfig();
			host = plugin.getConfig().getString("host");
			port = plugin.getConfig().getString("port");
			plugin.db = plugin.getConfig().getString("db");
			plugin.id = plugin.getConfig().getString("id");
			plugin.pw = plugin.getConfig().getString("pw");

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

}
