package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.db.DBManager;
import com.tools.Timediff;

public class Service {

    public Boolean login(String userid, String password) {

        // 获取Sql查询语句
        String logSql = "select * from user where StuID ='" + userid + "' and Password ='" + password + "'";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
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

    public Boolean register(String userid,String username, String password) {
    
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

    public boolean session(String guess, String userid, String time) {
        // 获取Sql查询语句
        String regSql;

        // 如果返回为空，即没有该该用户的session，则insert session
        // 否则updata
        if (loginwithoutpd(guess, userid).equals("null")) {
            regSql = "insert into LoginRecord values('"+ guess + "','"+ userid + "','"+ time+ "') ";
        } else {
            regSql = "UPDATA LoginRecord SET Time =" + time +" WHERE StuID = " + userid + " ;";
        }

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

    public String loginwithoutpd(String guess, String userid) {

        // 获取Sql查询语句
        String chkSql = "select * from LoginRecord where guess ='" + guess + "' and StuID ='" + userid + "'";

        // 获取DB对象
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(chkSql);
            if (rs.next()) {
                String stuid = rs.getString("StuID");
                String time = rs.getString("time");
                // 断开连接
                sql.closeDB();
                // 获取当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                // 转字符串
                String now_date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                // 计算上次登陆及这次的时间差
                long between = Timediff.timediff(time, now_date);
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
}