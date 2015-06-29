package com.github.circularmoonray.sqlstat;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.circularmoonray.sqlstat.commands.CAddPerms;
import com.github.circularmoonray.sqlstat.commands.CSql;
import com.github.circularmoonray.sqlstat.commands.CStat;

public class SqlStat extends JavaPlugin implements Listener {

	//集計用フラグ
	public boolean fCount = false;

	//このクラス自身を表すインスタンス
	public static SqlStat instance;

	//設定ファイル
	private Config config;

	//コマンドの一覧
	private HashMap<String, TabExecutor> commands;

	//統計用クラス
	public Stat stat;

	//集計用クラス
	public Count count;

	@Override
	public void onEnable(){
		instance = this;

		//コンフィグとjavaドライバーのロード
		config = Config.loadConfig();
		stat = new Stat(config);
		getLogger().info("config load completed" + config.getURL() + config.getDB());

		//コマンドの登録
		commands = new HashMap<String, TabExecutor>();
		commands.put("sql", new CSql(this));
		commands.put("stat", new CStat(this));
		commands.put("addperms", new CAddPerms(this));

		//リスナーの登録
		this.getServer().getPluginManager().registerEvents(new LJoinQuit(this), this);
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return commands.get(cmd.getName()).onCommand(sender, cmd, label, args);
	}

	@Override
	public void onDisable(){
		getLogger().info("Disable ");
	}

	//以下getter&setter
	public Stat getStat(){
		return stat;
	}

	public Config getconfig(){
		return config;
	}

}