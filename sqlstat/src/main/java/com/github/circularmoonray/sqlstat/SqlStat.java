package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SqlStat extends JavaPlugin implements Listener {
	public static SqlStat instance;


	private Stat stat;

	@Override
	public void onEnable(){
		instance = this;
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		this.reloadConfig();
		getLogger().info("config load completed");

		stat = new Stat(url, db, id, pw);

		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable(){
		getLogger().info("Disable ");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		stat.putMine(today, player);
	}



	public Stat getStat(){
		return stat;
	}

	//stat変数の初期化
	public Stat NewStat(){
		return new Stat(url, db, id, pw);
	}

	public String getPluginJarFile() {
		return this.getPluginJarFile();
	}

}