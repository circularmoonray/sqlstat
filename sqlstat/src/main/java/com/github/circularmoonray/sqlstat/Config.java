package com.github.circularmoonray.sqlstat;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private String db;
	private String id;
	private String pw;
	private String url;

	Config(){
		url = "jdbc:mysql://";
	}

	public static Config loadConfig(){
		Config tconfig = new Config();

		//ファイル位置の取得
		File configFile = new File(SqlStat.instance.getDataFolder(), "config.yml");

		//コンフィグファイルが存在しない場合はコピー
		if ( !configFile.exists() ) {
			SqlStat.instance.getConfig().options().copyDefaults(true);
			SqlStat.instance.saveConfig();
			SqlStat.instance.reloadConfig();
		}

		//コンフィグファイルの取得
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		tconfig.db = config.getString("db");
		tconfig.id = config.getString("id");
		tconfig.pw = config.getString("pw");

		tconfig.url += config.getString("host");
		if(!config.getString("port").isEmpty()){
			tconfig.url += ":" + config.getString("port");
		}

		return tconfig;
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

}
