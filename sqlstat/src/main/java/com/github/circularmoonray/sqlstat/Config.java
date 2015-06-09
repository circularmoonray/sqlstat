package com.github.circularmoonray.sqlstat;

import static com.github.circularmoonray.sqlstat.Param.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private String db;
	private String id;
	private String pw;
	private String url;
	private HashMap<String, Integer> resultset;
	private TreeMap<Integer, Rank> rankset;

	Config(){
		url = "jdbc:mysql://";
		resultset = new HashMap<String, Integer>();
		rankset = new TreeMap<Integer, Rank>();
	}

	public static Config loadConfig(){
		Config tconfig = new Config();

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
			tconfig.resultset.put(str, config.getInt(str));
		}

		for (String key : config.getConfigurationSection("rank").getKeys(false)) {
			Integer border = config.getInt("rank." + key + ".border");
			String name = config.getString("rank." + key + ".name");
			List<String> permissions = config.getStringList("rank." + key + ".permission");

			tconfig.rankset.put(border, new Rank(name, permissions));
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

	public TreeMap<Integer, Rank> getRankset() {
		return rankset;
	}

}
