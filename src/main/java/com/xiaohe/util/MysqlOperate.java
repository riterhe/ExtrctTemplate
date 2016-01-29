package com.xiaohe.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.management.Query;

import us.codecraft.webmagic.thread.CountableThreadPool;

public class MysqlOperate {

	public static boolean insert(String tableName, String value) throws SQLException {
		MysqlConnector myc = new MysqlConnector("tianchi");
		Connection conn = myc.conn;
		if (!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");
		// statement用来执行SQL语句
		Statement statement = conn.createStatement();
		String query = "";
		String sql = "insert into " + tableName + "(userID, age) values ('aaa',12);";
		System.out.println("sql=  " + sql);
		int count = statement.executeUpdate(sql);
		System.out.println("插入" + count + "条数据");
		conn.close();
		return true;
	}
	
	public static ArrayList<String> query(String SQLquery) throws SQLException {
		ArrayList<String> resultList = new ArrayList<String>();
		MysqlConnector myc = new MysqlConnector("tianchi");
		Connection conn = myc.conn;
		if (!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");
		// statement用来执行SQL语句
		Statement statement = conn.createStatement();
		ResultSet rs =  statement.executeQuery(SQLquery);
		while (rs.next()) {
			resultList.add(rs.getString("userID"));
		}
		conn.close();
		return resultList;
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		insert("test", "1");
		// 要执行的SQL语句
		String SQLquery = "select * from test limit 4;";
		ArrayList<String> resultList = query(SQLquery);
		for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
	}
}
