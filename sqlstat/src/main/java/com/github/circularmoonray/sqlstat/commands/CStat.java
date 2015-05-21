package com.github.circularmoonray.sqlstat.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.github.circularmoonray.sqlstat.SqlStat;

public class CStat implements TabExecutor {
	private SqlStat plugin;

	CStat(SqlStat plugin){
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
				StatPut(sender, cmd, label, args);
				return true;
			}
		}

		return false;
	}

	private void StatStart(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		// メインスコアボードを取得します。
        ScoreboardManager manager = plugin.getServer().getScoreboardManager();
        Scoreboard mboard = manager.getMainScoreboard();
        Scoreboard nboard = manager.getMainScoreboard();
 
        // オブジェクティブが既に登録されているかどうか確認し、
        // 登録されていないなら新規作成します。
        Objective smine = board.getObjective(OBJECTIVE_NAME);
        
        if ( objective == null ) {
            objective = board.registerNewObjective(OBJECTIVE_NAME, "health");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName("/ 20");
        }
		

	}

	private void StatPut(CommandSender sender, Command cmd, String label,
			String[] args) {

		String s = "";

		s = String.valueOf( ((Player) sender).getStatistic(Statistic.USE_ITEM, Material.CARROT_ITEM) );
		sender.sendMessage("あなたの石の掘削数:" + s);

	}

	

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {

		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
