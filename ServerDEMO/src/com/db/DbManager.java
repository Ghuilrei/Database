package com.db;
 
import java.sql.*;

/**
 * @description 数据库管理类
 * @author Ghuilrei
 * @date 2020/7/9 17:38
 * @version V1.0
 */

public class DbManager {

    /** 数据库驱动 **/
//    public static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /** 用户名、密码 **/
    public static final String USER = "JavaWeb_Android";
    public static final String PASS = "aktwthTK8sqaPGPs";

    /** 数据库地址 **/
//    public static final String URL = "jdbc:mysql://localhost:3306/Library";
    public static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=Library";

    /**
     * 静态成员，支持单态模式
     */
    private static DbManager per = null;
    private Connection conn = null;
    private Statement stmt = null;

    /**
     * @description 单态模式-懒汉模式
     */
    private DbManager() {
    	
    }

    /**
     * @description 创建
     */
    public static DbManager createInstance() {
        if (per == null) {
            per = new DbManager();
            per.initDataBase();
        }
        return per;
    }

    /**
     * @description 加载数据库驱动
     */
    public void initDataBase() {
        try {
            Class.forName("DRIVER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 连接数据库，获取句柄+对象
     */
    public void connectDataBase() {
        // FIXME System.out.println
        System.out.println("DbManager:55:SqlManager:Connecting to database...");
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // FIXME System.out.println
        System.out.println("DbManager:55:SqlManager:Connect to database successful.");
    }

    /**
     * @description 关闭数据库 关闭对象，释放句柄
     */
    public void closeDataBase() {
        // FIXME System.out.println
        System.out.println("DbManager:55:Close connection to database..");
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // FIXME System.out.println
        System.out.println("DbManager:61:Close connection successful");
    }

    /**
     * @description 查询
     */
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * @description 增添/删除/修改
     */
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