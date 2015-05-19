package com.github.circularmoonray.sqlstat;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SqlStat extends JavaPlugin implements Listener {
	public static SqlStat instance;

	//コンフィグの宣言
	private Config config;

	//コマンド一覧
	private HashMap<String, TabExecutor> commands;

	//集計コマンドのフラグ
	private boolean statflag = false;

	public Stat stat;

	@Override
	public void onEnable(){
		instance = this;

		//コンフィグのロード
		config = Config.loadConfig();
		stat = new Stat(config);
		getLogger().info("config load completed");

		//コマンドの登録
		commands = new HashMap<String, TabExecutor>();
		commands.put("sql", new CSql(this));
		commands.put("stat", new CStat(this));

		//リスナーの登録
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

	public boolean getStatFlag(){
		return statflag;
	}

	public void setStatFlag(boolean statflag){
		this.statflag = statflag;
	}

}