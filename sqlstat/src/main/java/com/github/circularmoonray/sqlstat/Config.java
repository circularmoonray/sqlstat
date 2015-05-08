package com.github.circularmoonray.sqlstat;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private String db;
	private String id;
	private String pw;
	private String url;
	private File configFile;

	Config(){

	}

	public String getDB(){
		return db;
	}

	public String getID(){
		return id;
	}

	public String getPW(){
		return pw;
	}

	public String getURL(){
		return url;
	}

	public Config loadConfig(){

		File configFile = new File(SqlStat.instance.getDataFolder(), "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

	}

}
