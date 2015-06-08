package com.github.circularmoonray.sqlstat;

import java.util.Arrays;
import java.util.List;

public class Param {

	private Param(){}

	//MySQL内のテーブル名
	public static final String today = "today";
	public static final String past = "past";

	//集計コマンド用のスコアボード名
	public static final String smine = "smine";
	public static final String emine = "emine";
	public static final String mine = "mine";

	//各統計データ
		public static final List<String> statlist = Arrays.asList(
				"stone",
				"netherrack",
				"dirt",
				"gravel",
				"log",
				"seed",
				"potate",
				"carrot");

}
