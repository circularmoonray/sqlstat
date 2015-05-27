package com.github.circularmoonray.sqlstat;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Count {
	SqlStat plugin;

	private HashMap<UUID, Integer> smine;
	private HashMap<UUID, Integer> emine;

	public Count(SqlStat plugin){
		this.plugin = plugin;
		smine = new HashMap<UUID, Integer>();
		emine = new HashMap<UUID, Integer>();
	}

	//スタート時の集計データsmineに値を挿入
	public int setSmine(Player player, int param){
		return setMine(smine, player.getUniqueId(), param);
	}
	public int setSmine(UUID uuid, int param){
		return setMine(smine, uuid, param);
	}

	//スタート時の集計データemineに値を挿入
	public int setEmine(Player player, int param){
		return setMine(emine, player.getUniqueId(), param);
	}
	public int setEmine(UUID uuid, int param){
		return setMine(emine, uuid, param);
	}

	//初期値はNULLが返されるので、NULLの場合は、-1を返す
	private int setMine(HashMap<UUID, Integer> mine, UUID uuid, int param){
		try{
			return mine.put(uuid, param);
		}catch(NullPointerException e){
			return -1;
		}
	}

	/**
	 * 集計データの差分をソートして渡します
	 * @return 集計データの差分のハッシュマップ<Integer, UUID>
	 * @throws NullPointerException
	 */
	public TreeMap<Integer, UUID> calcDiff() throws NullPointerException{

		//計算結果を格納(TreeMapの為自動ソートされる)
		TreeMap<Integer, UUID> mine = new TreeMap<Integer, UUID>();

		int diff = 0;
		UUID uuid;

		for(Entry<UUID, Integer> e : emine.entrySet()){

			//参照している終了時の集計データからUUIDを取得
			uuid = e.getKey();

			//終了時の集計データ又は開始時の集計データが存在しなかったら、NullPointerExceptionを返す
			if((uuid == null) || (smine.get(uuid) == null)){
				throw new NullPointerException("calcDiff : NullException");

			//値が存在したら、集計データの差分を格納
			}else if(e.getValue() >= 0 && smine.get(uuid).intValue() >= 0){
				diff = e.getValue() - smine.get(uuid).intValue();
				mine.put(diff, uuid);
			}
		}

		return mine;
	}
}
