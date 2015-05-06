package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SqlStat extends JavaPlugin implements Listener {
	String db;
	private String id;
	private String pw;
	private String url;

	public static Stat stat;

	@Override
	public void onEnable(){
		LoadConfig();
		stat = new Stat(url, db, id, pw);

		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new CommandSql(this), this);
	}

	@Override
	public void onDisable(){
		getLogger().info("onDisableメソッドが呼び出されたよ！！");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		stat.putMine(today, player);
	}





	//configの読み込み
	private boolean LoadConfig(){
		String host;
		String port;

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

	//stat変数の初期化
	public boolean NewStat(){
		stat = new Stat(url, db, id, pw);
		return true;
	}




}