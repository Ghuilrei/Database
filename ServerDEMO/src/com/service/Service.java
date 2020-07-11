package com.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.db.DbManager;
import com.tools.MD5;
import com.tools.Timediff;

/**
 * @description 操作数据库
 * @author Ghuilrei
 * @date 2020/7/9 17:38
 * @version V1.0
 */

public class Service {

    /**
     * @description 使用Session登陆
     * @param session 字符串：session
     * @param userId 字符串：用户id（学号或管理员号）
     * @return String：
     *      登陆成功: "{[session:****],[userId:****],[name:****],[ismanager:****]}"
     *      登陆失败：错误码
     */
    public String sessionLogin(String userId, String session) {
        // TODO System.out.print
        System.out.println("sessionLogin Launched");

        String chkSql = "SELECT * FROM loginrecord WHERE user_id = '"+ userId +"' AND session = '"+ session +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 操作DB对象
        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 如果有数据
            if (rs.next()) {

                // 获取时间
                String time = rs.getString("last_date");

                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // new Date()为获取当前系统时间，也可使用当前时间戳
                String nowDate = df.format(new Date());
                // 计算上次登陆及这次的时间差
                long between = Timediff.timediff(time, nowDate);

                // 判断天数（7天）
                int weekToDay = 604800;
                if (between <= weekToDay) {
                    session = updataSession(userId);
                    if ("0301".equals(session)){
                        return session;
                    } else {
                        // 查询基础信息
                        chkSql = "SELECT * FROM userbase WHERE user_id = '"+ userId +"';";
                        rs = sql.executeQuery(chkSql);

                        // 获取姓名、身份和状态
                        String name = rs.getString("user_name");
                        String isManager = rs.getString("is_manager");
                        String isBan = rs.getString("is_ban");

                        // 断开连接
                        sql.closeDataBase();
                        return "{[session:"+session+"],[userId:"+userId+"],[name:"+name+"],[ismanager:"+isManager+"],,[isban:"+isBan+"]}";
                    }
                } else {
                    return "0101";
                }
            } else {
                // 如果没有数据
                return "0102";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            return "0103";
        }
    }

    /**
     * @description 使用账号密码登陆
     * @param userId 字符串：用户id（学号或管理员号）
     * @param password 字符串：密码
     * @return String :
     *      登陆成功:  "{[session:****],[userId:****],[name:****],[ismanager:****]}"
     *      登陆失败：错误码
     */
    public String passwordLogin(String userId, String password) {

        String logSql = "SELECT * FROM loginrecord WHERE user_id = '"+ userId +"' AND password = '"+ password +"';";

        // TODO passwordLogin的SQL语句
        System.out.println("passwordLogin的SQL语句:"+logSql);

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);

            // 如果查询有数据
            if (rs.next()) {

                // 查询基础信息
                logSql = "SELECT * FROM userbase WHERE user_id = '"+ userId +"';";
                rs = sql.executeQuery(logSql);

                // 获取姓名、身份和状态
                String name = rs.getString("user_name");
                String isManager = rs.getString("is_manager");
                String isBan = rs.getString("is_ban");

                // 断开连接
                sql.closeDataBase();
                String session = updataSession(userId);
                if ("0301".equals(session)){
                    return session;
                } else {
                    return "{[session:"+session+"],[userId:"+userId+"],[name:"+name+"],[ismanager:"+isManager+"],,[isban:"+isBan+"]}";
                }
            } else {
                // 断开连接
                sql.closeDataBase();
                return "0201";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            return "0202";
        }
    }

    /**
     * @description 更新session
     * @param userId 字符串：用户id（学号或管理员号）
     * @return String :
     *      session  : 更新session成功
     *      错误码    : 更新session失败
     */
    public String updataSession(String userId) {
        // TODO updataSession Launched
        System.out.println("updataSession Launched");

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        String date = df.format(new Date());
        // 获取session
        String session = MD5.getMD5String(date+userId);

        String upSql = "UPDATE loginrecord SET session = '"+ session +"', last_date = '"+date+"' WHERE user_id = '"+ userId +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(upSql);
        if (ret != 0) {
            sql.closeDataBase();
            return session;
        }
        sql.closeDataBase();
        return "0301";
    }

    /**
     * @description: 注册
     * @param userId 字符串：用户id（学号或管理员号）
     * @param password 字符串：密码
     * @param isManager 身份
     * @return boolean :
     *      0400  : 注册成功
     *      0401  : 注册失败
     */
    public String regester(String userId, String userName, String password, String isManager) {
        // TODO regester Launched
        System.out.println("regester Launched");

        // 获取Sql查询语句
        String regSql = "INSERT INTO loginrecord(user_id, session, password)  ('"+userId+"', null, '"+password+"');";
        String regSql1 = "UPDATE user SET user_name = '"+ userName +"'";
        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(regSql) + sql.executeUpdate(regSql1);
        if (ret == 2) {
            sql.closeDataBase();
            return "0400";
        }
        sql.closeDataBase();
        return "0401";
    }

    /**
     * @description: 验证管理员
     * @param userId 字符串：用户id（学号或管理员号）
     * @return boolean :
     *      0400  : 是管理员
     *      0401  : 不是管理员
     */
    public boolean verifyManager(String userId) {
        // TODO verifyManager Launched
        System.out.println("verifyManager Launched");

        String chkSql = "SELECT * FROM manager_session WHERE user_id = '"+ userId +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 操作DB对象
        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 如果有数据
            if (rs.next()) {

                // 获取学号、Session和时间
                return rs.getBoolean("is_manager");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
        }
        return false;
    }

    /**
     * @description 验证Session登陆
     * @param session 字符串：session
     * @param userId 字符串：用户id
     * @return String：
     *      登陆成功: 0600
     *      登陆失败：错误码
     */
    public String verifyLogin(String userId, String session) {
        // TODO System.out.print
        System.out.println("verifyLogin Launched");

        String chkSql = "SELECT * FROM manager_session WHERE user_id = '"+ userId +"' AND session = '"+ session +"';";

        // TODO sessionLogin 的SQL语句
        System.out.println("sessionLogin 的SQL语句:"+chkSql);

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 操作DB对象
        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 如果有数据
            if (rs.next()) {

                // 获取学号、Session和时间
                String time = rs.getString("last_date");

                // 断开连接
                sql.closeDataBase();

                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // new Date()为获取当前系统时间，也可使用当前时间戳
                String nowDate = df.format(new Date());
                // 计算上次登陆及这次的时间差
                long between = Timediff.timediff(time, nowDate);

                // 判断天数（7天）
                int weekToDay = 604800;
                if (between <= weekToDay) {
                    return "0600";
                } else {
                    return "0601";
                }
            } else {
                // 如果没有数据
                return "0602";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            return "0603";
        }
    }

    /**
     * @description 上架书籍
     * @param book_name 书名
     * @param categoryID 所属类号
     * @param author 作者
     * @param price 价格
     * @param press 出版社
     * @param public_date 出版时间
     * @param remain 库存
     * @return String：
     *      成功: "0700"
     *      失败："0701"
     */
    public String addBook(String categoryID, String book_name, String author, String price, String press, String public_date, String remain) {

        // TODO addBook Launched
        System.out.println("addBook Launched");

        // 获取Sql查询语句
        String regSql = "INSERT INTO bookinfo(categoryID, book_name, author, price, press, public_date, remain) VALUES ('"+ categoryID +"', '"+ book_name +"', '"+ author +"', '"+ price +"', '"+ press +"', '"+ public_date +"', '"+ remain +"')";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDataBase();
            return "0700";
        }
        sql.closeDataBase();
        return "0701";
    }

    /**
     * @description 下架书籍
     * @param bookId 书籍号
     * @return String：
     *      成功: "0800"
     *      失败："0801"
     */
    public String delBook(String bookId) {
        // TODO addBook Launched
        System.out.println("delBook Launched");

        // 获取Sql查询语句
        String regSql = "DELETE FROM bookinfo WHERE book_id = '"+ bookId +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDataBase();
            return "0800";
        }
        sql.closeDataBase();
        return "0801";
    }
}