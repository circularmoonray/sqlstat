package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class CStat implements TabExecutor {
	private SqlStat plugin;

	CStat(SqlStat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args.length == 2){
			if(args[0].equalsIgnoreCase("start")){
				return StatStart(sender, cmd, label, args);
			}
		}

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
			}

		}else if(args.length == 0){
			StatNonOption(sender, cmd, label, args);

		}

		return false;
	}

	private boolean StatStart(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		ScoreboardManager manager = plugin.getServer().getScoreboardManager();
		Scoreboard mboard = manager.getMainScoreboard();
		Scoreboard nboard = manager.getNewScoreboard();

		Objective smine = nboard.getObjective(scoreSMine);
		Objective emine = nboard.getObjective(scoreEMine);
		
		if ( smine == null || emine == null) {
			smine = nboard.registerNewObjective(scoreSMine, "dummy");
			emine = nboard.registerNewObjective(scoreEMine, "dummy");
			
			smine.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		
		return false;
	}


	private void StatNonOption(CommandSender sender, Command cmd, String label,
			String[] args) {

		String s = "";

		s = String.valueOf( ((Player) sender).getStatistic(Statistic.USE_ITEM, Material.CARROT_ITEM) );
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
