package com.github.circularmoonray.sqlstat.commands;

import static com.github.circularmoonray.sqlstat.Utilities.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.circularmoonray.sqlstat.Count;
import com.github.circularmoonray.sqlstat.SqlStat;

public class CStat implements TabExecutor {
	private SqlStat plugin;

	public CStat(SqlStat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args.length > 1){
			sender.sendMessage("引数は1つにまでにして下さい");
			return true;

		}else if(args.length == 1){

			//集計スタート
			if(args[0].equalsIgnoreCase("start")){
				StatStart(sender, cmd, label, args);
				return true;

			//集計データの表示
			}else if(args[0].equalsIgnoreCase("put")){
				StatPut(sender, cmd, label, args);
				return true;

			}else if(args[0].equalsIgnoreCase("end")){
				StatEnd(sender, cmd, label, args);
				return true;
			}
		}

		return false;
	}

	private void StatStart(CommandSender sender, Command cmd, String label,
			String[] args) {
		plugin.count = new Count(plugin);
		Count count = plugin.count;

		plugin.fCount = true;

		for(Player player : plugin.getServer().getOnlinePlayers()){
			count.setSmine(player, (player).getStatistic(Statistic.MINE_BLOCK, Material.STONE));
		}

		sendOPMessage(ChatColor.BLUE + "集計を開始しました。");
	}

	private void StatPut(CommandSender sender, Command cmd, String label,
			String[] args) {

		if(plugin.count != null){
			Integer i = 1;
			Count count = plugin.count;
			TreeMap<Integer, UUID> mine = new TreeMap<Integer, UUID>();

			for(Player player : plugin.getServer().getOnlinePlayers()){
				count.setEmine(player, (player).getStatistic(Statistic.MINE_BLOCK, Material.STONE));
			}

			mine = count.calcDiff();

			sendEveryMessage(ChatColor.BLUE + "集計結果を発表します。");
			for(Map.Entry<Integer, UUID> map : mine.entrySet()){

				if(i < 10){
					sendEveryMessage(i.toString() +
							"位 " +
							plugin.getServer().getPlayer(map.getValue()).getName() +
							" : " +
							map.getKey().toString());
				}

				i++;
			}

		}else{
			sender.sendMessage(ChatColor.RED + "集計が開始されていません！");
		}

	}

	private void StatEnd(CommandSender sender, Command cmd, String label,
			String[] args) {
		StatPut(sender, cmd, label, args);

		plugin.fCount = false;
		plugin.count = null;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {

		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
