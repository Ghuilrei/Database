package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.db.DBManager;
import com.tools.Timediff;

public class Service {

    /**
     * @description:
     * @param session 字符串：session
     * @param userid 字符串：用户id（学号或管理员号）
     * @return 返回长整形，单位秒
     */
    public static String loginwithoutpd(String session, String userid) {

        // 获取Sql查询语句
        String chkSql = "select * from LoginRecord where StuID ='" + userid + "'";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);
            // 如果有数据
            if (rs.next()) {
                // 获取学号和时间
                String stuid = rs.getString("StuID");
                String sesssion = rs.getString("Session");
                String time = rs.getString("time");
                // 断开连接
                sql.closeDB();
                // 获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                // 转字符串
                // new Date()为获取当前系统时间，也可使用当前时间戳
                String now_date = df.format(new Date());
                // 计算上次登陆及这次的时间差
                long between = Timediff.timediff(time, now_date);

                // FIXME System.out.println
                System.out.println("Service:115:stuid:"+stuid);

                // 判断天数
                if (between >= 604800) {
                    return "false";
                } else
                    return stuid;
            } else {
                return "null";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.closeDB();
        return "err";
    }

    public static boolean session(String guess, String userid, String time) {

        // 获取Sql查询语句
        String regSql;
        boolean pd = loginwithoutpd(guess, userid).equals("null");

        // 如果返回为空，即没有该该用户的session，则insert session
        // 否则updata
        if (pd) {
            regSql = "insert into LoginRecord values('"+ guess + "','"+ userid + "','"+ time+ "') ";
        } else {
            regSql = "UPDATE LoginRecord SET guess='" + guess + "', Time='" + time + "' WHERE StuID='" + userid + "' ;";
        }
        // FIXME System.out.println
        System.out.println("Service:67:regSql:"+regSql);
        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        return false;
    }

    // 登陆
    public static Boolean login(String userid, String password) {
        // 获取Sql查询语句
        String logSql = "select * from user where StuID ='" + userid + "' and Password ='" + password + "'";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
            // 如果查询有数据
            if (rs.next()) {
                sql.closeDB();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.closeDB();
        return false;
    }

    public static Boolean register(String userid,String username, String password) {

        // 获取Sql查询语句
        String regSql = "insert into user values('"+ userid + "','"+ username + "','"+ password+ "') ";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        return false;
    }
}