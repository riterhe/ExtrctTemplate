package com.xiaohe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {
	public static final String url = "jdbc:mysql://127.0.0.1:3306/";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "riter";  
    public static final String password = "xiaopigu";  
    
    public Connection conn = null;  
  
    public MysqlConnector(String databaseName) {
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url + databaseName, user, password);//获取连接  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void close() {  
        try {  
            this.conn.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    } 
}
