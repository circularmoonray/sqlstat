package com.github.circularmoonray.sqlstat;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CStat implements TabExecutor {
	private SqlStat plugin;

	CStat(SqlStat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("このコマンドはプレイヤーのみ実行できます");
			return true;

		}else if(args.length > 1){
			sender.sendMessage("引数は1つにまでにして下さい");
			return true;

		}else if(args.length == 1){
			//statのallオプション
			if(args[0].equalsIgnoreCase("all")){
				StatAll(sender, cmd, label, args);
				return true;

			//statのsetオプション
			}else if(args[0].equalsIgnoreCase("set")){
				((Player) sender).setStatistic(Statistic.KILL_ENTITY, EntityType.ENDERMAN, 10);
				return true;

			}else{
				return false;
			}

		}else if(args.length == 0){
			StatNonOption(sender, cmd, label, args);

		}

		return false;

	}

	private void StatNonOption(CommandSender sender, Command cmd, String label,
			String[] args) {

		String s = "";

		s = String.valueOf( ((Player) sender).getStatistic(Statistic.KILL_ENTITY, EntityType.ENDER_DRAGON) );
		sender.sendMessage("あなたの石の掘削数:" + s);

	}

	private void StatAll(CommandSender sender, Command cmd, String label,
			String[] args) {

		String s = "";
		int istat = 0;

		//オンラインプレイヤーのスコア取得
		for(Player player : plugin.getServer().getOnlinePlayers()){
			istat = SqlStat.instance.stat.getStat(player, Material.STONE);
			s = player.getDisplayName() + ":" + String.valueOf(istat);
			sender.sendMessage(s);
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {

		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
