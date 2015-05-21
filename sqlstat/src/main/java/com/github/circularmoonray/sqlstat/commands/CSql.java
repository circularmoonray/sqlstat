package com.github.circularmoonray.sqlstat.commands;

import static com.github.circularmoonray.sqlstat.Param.*;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.circularmoonray.sqlstat.Config;
import com.github.circularmoonray.sqlstat.SqlStat;
import com.github.circularmoonray.sqlstat.Stat;

public class CSql implements TabExecutor {
	private SqlStat plugin;

	public CSql(SqlStat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Stat stat = plugin.getStat();

		if(cmd.getName().equalsIgnoreCase("sql")){

			//オプションの判定
			if(args.length > 1){
				sender.sendMessage("引数は1つにまでにして下さい");
				return false;

			//configのリロードオプション
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("reload")){
					Config.loadConfig();
					sender.sendMessage("reload completed");
					return true;

				//現在ログイン中のプレイヤーの実績をSQLに書き出し
				}else if(args[0].equalsIgnoreCase("mine")){
					for(Player player : plugin.getServer().getOnlinePlayers()){
						stat.putMine(today, player);
						stat.putFarming(today, player);
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





	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

}