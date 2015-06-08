package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private String db;
	private String id;
	private String pw;
	private String url;
	private HashMap<String, Integer> resultset;
	private List<String> rank;
	private Set<String> keys;

	Config(){
		url = "jdbc:mysql://";
		resultset = new HashMap<String, Integer>();
	}

	public static Config loadConfig(){
		Config tconfig = new Config();
		tconfig.keys = new HashSet<String>();

		//ファイル位置の取得
		File configFile = new File(SqlStat.instance.getDataFolder(), "config.yml");

		//存在しない場合はコピー
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

		for (String str : statlist) {
			tconfig.getResultset().put(str, config.getInt(str));
		}

		for (String key : config.getConfigurationSection("rank").getKeys(false)) {
			tconfig.keys.add(key);
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

	public HashMap<String, Integer> getResultset() {
		return resultset;
	}

	public List<String> getRank() {
		return rank;
	}

	public Set<String> getKeys(){
		return keys;
	}

}
