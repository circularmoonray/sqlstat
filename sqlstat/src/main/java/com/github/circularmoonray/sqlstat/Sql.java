package com.github.circularmoonray.sqlstat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

//MySQL操作関数
public class Sql{

	private final String url, db, id, pw;

	private Connection con = null;
	private Statement stmt = null;
	private ResultSet result = null;
	public static String exc;
	UUID uuid;


	Sql(String url, String db, String id, String pw){
		this.url = url;
		this.db = db;
		this.id = id;
		this.pw = pw;

		String command = "";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}

		//sql鯖への接続とdb作成
		connect(url, id, pw);


		//dbへの接続。失敗時には作成
		command = "USE " + db;

		try {
			stmt.execute(command);
		} catch (SQLException e) {
			exc = e.getMessage();
			createDB(db);
			connect(url, id, pw);
			try {
				stmt.execute(command);
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
	}

	//接続関数
	private boolean connect(String url, String id, String pw){
		try {
			con = (Connection) DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
		}
		return true;
	}

	//テーブル作成。失敗時にはエラーコードを@excに格納
	public boolean createTable(String table, String name, int statstone){
		String command = "";
		command = "create table " + table +
				"(id int auto_increment unique, " +
				"name varchar(30), " +
				"stone int default 0, " +
				"netherrack int default 0, " +
				"gravel int default 0, " +
				"dirt int default 0, " +
				"death int default 0, " +
				"break_dPickaxe int default 0, " +
				"use_seed int default 0, " +
				"uuid varchar(128) unique);";
		try{
			stmt.execute(command);
			return true;
		} catch (SQLException e) {
			exc = e.getMessage();
		}
		return false;
	}


	//テーブルにデータを追加。失敗時にはエラーコードを@excに格納
	public boolean insert(String table, String key, int stat, UUID uuid){
		String command = "";
		String struuid = uuid.toString();
		String s = String.valueOf(stat);

		//command:
		//insert into @table(@key, uuid) values(@s, '@struuid')
		// on duplicate key update @key=@s
		command = "insert into " +  table +
				"(" + key + ", uuid) values(" +
				s + ", '" + struuid + "')" +
				" on duplicate key update " + key + "=" + s;
		try {
			stmt.executeUpdate(command);
			return true;
		} catch (SQLException e) {
			exc = e.getMessage();
		}
		return false;
	}

	public boolean insert(String table, String key, String s, UUID uuid){
		String command = "";
		String struuid = uuid.toString();

		//command:
		//insert into @table(@key, uuid) values(@s, '@struuid')
		// on duplicate key update @key=@s
		command = "insert into " +  table +
				"(" + key + ", uuid) values('" +
				s + "', '" + struuid + "')" +
				" on duplicate key update " + key + "='" + s + "'";
		try {
			stmt.executeUpdate(command);
			return true;
		} catch (SQLException e) {
			//接続エラーの場合は、再度接続後、コマンド実行
			exc = e.getMessage();
			connect(url, id, pw);
			try {
				stmt.executeUpdate(command);
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
		return false;
	}

	public boolean disconnect(){
	    if (con != null){
	    	try{
	    		stmt.close();
				con.close();
	    	}catch (SQLException e){
	    		e.printStackTrace();
	    		return false;
	    	}
	    }
	    return true;
	}

	public boolean createDB(String str){
		boolean re;
		if(str==null){
			return false;
		}

		try {
			re = stmt.execute("CREATE DATABASE IF NOT EXISTS " + str);
			return re;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}