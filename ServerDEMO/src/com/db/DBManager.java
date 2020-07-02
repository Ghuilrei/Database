package com.db;
 
import java.sql.*;
 
public class DBManager {
 
    // 数据库连接常量
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "root";
    public static final String PASS = "Sunyuhang0616";
    public static final String URL = "jdbc:mysql://localhost:3306/JavaWeb_Android";
 
    // 静态成员，支持单态模式
    private static DBManager per = null;
    private Connection conn = null;
    private Statement stmt = null;
 
    // 单态模式-懒汉模式
    private DBManager() {
    	
    }
 
    public static DBManager createInstance() {
        if (per == null) {
            per = new DBManager();
            per.initDB();
        }
        return per;
    }
 
    // 加载驱动
    public void initDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // 连接数据库，获取句柄+对象
    public void connectDB() {
        // FIXME System.out.println
        System.out.println("DBManager:55:SqlManager:Connecting to database...");
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // FIXME System.out.println
        System.out.println("DBManager:55:SqlManager:Connect to database successful.");
    }
 
    // 关闭数据库 关闭对象，释放句柄
    public void closeDB() {
        // FIXME System.out.println
        System.out.println("DBManager:55:Close connection to database..");
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // FIXME System.out.println
        System.out.println("DBManager:61:Close connection successful");
    }
 
    // 查询
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
 
    // 增添/删除/修改
    public int executeUpdate(String sql) {
        int ret = 0;
        try {
            ret = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}