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

    // 系统功能

    /**
     * @description: 注册
     * @param phone 字符串：用户手机号
     * @param password 字符串：密码
     * @param isManager 身份
     * @return String：A01**
     */
    public String register(String phone, String userName, String password, String isManager) {

        // 获取Sql查询语句
        String regSql = "INSERT INTO loginrecord(phone, password) values ('"+phone+"', '"+password+"');";
        String regSql1 = "UPDATE user SET user_name = '"+ userName +"' WHERE phone = '"+ phone +"';;";
        String regSql2 = "UPDATE user SET user_name = '"+ userName +"', is_manager = 1 WHERE phone = '"+ phone +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 执行sql
        int ret = sql.executeUpdate(regSql);
        int ret1;

        // 判断是否为管理员
        if ("true".equals(isManager)) {
            ret1= sql.executeUpdate(regSql1);
        } else {
            ret1 = sql.executeUpdate(regSql2);
        }
        if (ret != 0 && ret1 != 0) {
            sql.closeDataBase();
            // TODO return code A0100
            System.out.println("A0100");
            return "A0100";
        }
        sql.closeDataBase();
        // TODO return code A0101
        System.out.println("A0101");
        return "A0101";
    }

    /**
     * @description 使用账号密码登陆
     * @param phone 字符串：用户手机号
     * @param password 字符串：密码
     * @return String ：02**
     */
    public String passwordLogin(String phone, String password) {

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // SQL语句
        String logSql = "SELECT * FROM loginrecord WHERE phone = '"+ phone +"' AND password = '"+ password +"';";

        // 操作DB对象
        try {

            // 执行sql
            ResultSet rs = sql.executeQuery(logSql);

            // 如果查询有数据
            if (rs.next()) {
                // 断开连接
                sql.closeDataBase();
                // TODO return code A0200
                System.out.println("A0200");
                return "A0200";
            } else {
                // 断开连接
                sql.closeDataBase();
                // TODO return code A0201
                System.out.println("A0201");
                return "A0201";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            // TODO return code A0201
            System.out.println("A0201");
            return "A0202";
        }
    }

    /**
     * @description 使用Session登陆
     * @param session 字符串：session
     * @param userId 字符串：用户id（学号或管理员号）
     * @return String：A03**
     */
    public String sessionLogin(String userId, String session) {

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // SQL语句
        String chkSql = "SELECT * FROM loginrecord WHERE user_id = '"+ userId +"' AND session = '"+ session +"';";

        // 操作DB对象
        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 如果有数据，即登陆成功
            if (rs.next()) {

                // 获取时间
                String time = rs.getString("last_date");

                // 关闭链接
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
                    // 登陆成功
                    // TODO return code A0300
                    System.out.println("A0300");
                    return "A0300";
                } else {
                    // 登陆过期
                    // TODO return code A0301
                    System.out.println("A0301");
                    return "A0301";
                }
            } else {
                sql.closeDataBase();
                // TODO return code A0302
                System.out.println("A0302");
                // 如果没有数据
                return "A0302";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            // TODO return code A0303
            System.out.println("A0303");
            return "A0303";
        }
    }

    /**
     * @description 更新session
     * @param userId 字符串：用户id（学号或管理员号）
     * @return String :
     *      session : 更新session成功
     *      A04**    : 更新session失败
     */
    public String updateSession(String phone, String userId) {

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        String date = df.format(new Date());

        // 获取session
        String session = MD5.getMD5String(date+userId);

        // 将session加入到loginrecord中
        String upSql = "UPDATE loginrecord SET session = '"+ session +"', last_date = '"+date+"' WHERE user_id = '"+ userId +"' OR  phone = '"+ phone +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(upSql);
        if (ret != 0) {
            sql.closeDataBase();
            // TODO return code A0400
            System.out.println("A0400");
            return session;
        }
        sql.closeDataBase();
        // TODO return code A0401
        System.out.println("A0401");
        return "A0401";
    }

    /**
     * @description: 验证管理员
     * @param userId 字符串：用户id（学号或管理员号）
     * @return boolean :
     */
    public String verifyManager(String userId) {

        String back;

        String chkSql = "SELECT is_manager FROM userbase WHERE user_id = '"+ userId +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 操作DB对象
        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 如果有数据
            if (rs.next()) {

                back = rs.getBoolean("is_manager") ? "A0500" : "A0501";

                // TODO return code A0500/A0501
                System.out.println(back );

                // 获取学号、Session和时间
                return back;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
        }
        return "A0502";
    }

    /**
     * @description: 查询基本信息
     * @param user_id 字符串：用户id
     * @param phone 用户手机号
     * @return String：A06**
     *      "[userId:"+user_id+"],[name:"+name+"],[ismanager:"+isManager+"],[isban:"+isBan+"],[phone:"+phone+"]"
     */
    public String UserBase(String user_id, String phone) {


        // 查询基础信息
        String logSql = "SELECT * FROM userbase WHERE user_id = '"+ user_id +"' OR phone = '"+ phone +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        // 操作DB对象
        try {
            // 查询结果
            ResultSet rs = sql.executeQuery(logSql);

            // 如果有数据
            if (rs.next()) {

                // 获取姓名、身份和状态
                user_id = rs.getString("user_id");
                phone = rs.getString("phone");
                String name = rs.getString("user_name");
                String isManager = rs.getString("is_manager");

                // 更新用户状态
                String logSql0 = "CALL refreshis_ban("+ user_id +", @result);";
                String logSql1 = "SELECT @result";
                // 查询结果对象
                sql.executeUpdate(logSql0);
                rs = sql.executeQuery(logSql1);
                rs.next();
                // 获取装好状态
                String isBan = rs.getString("@result");

                // 断开连接
                sql.closeDataBase();

                // TODO return code A0600
                System.out.println("A0600");

                // 获取学号、Session和时间
                return "[user_id:"+user_id+"]," +
                        "[user_name:"+name+"]," +
                        "[is_manager:"+isManager+"]," +
                        "[is_ban:"+isBan+"]," +
                        "[phone:"+phone+"]";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
        }
        // TODO return code A0601
        System.out.println("A0601");
        return "A0601";
    }

    /**
     * @description 退出登录
     * @param userId 字符串：用户id（学号或管理员号）
     * @return String : 错误码
     */
    public String logOff(String userId) {


        // 将session加入到loginrecord中
        String upSql = "UPDATE loginrecord SET session = null WHERE user_id = '"+ userId +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(upSql);
        if (ret != 0) {
            sql.closeDataBase();
            // TODO return code A0700
            System.out.println("A0700");
            return "A0700";
        }
        sql.closeDataBase();
        // TODO return code A0701
        System.out.println("A0701");
        return "A0701";
    }



    // 用户功能

    /**
     * @description 查询图书
     * @param book_name 书名
     * @param category_name 所属类号
     * @param author 作者
     * @param press 出版社
     * @param public_date 出版时间
     * @return String[]：每个字符串存一本书的信息
     */
    public String[] checkBook(String book_id, String category_name, String book_name, String author, String press, String public_date) {

        // 返回值
        String[] back;

        // 查询书的信息
        String chkSql = "SELECT * FROM bookbase WHERE book_id = '"+ book_id +"' OR category_name = '"+ category_name +"' OR book_name = '"+ book_name +"' OR author = '"+ author +"' OR press = '"+ press +"' OR public_date = '"+ public_date +"';";


        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 把指针放到最后
            rs.last();
            // 获取最后一行的行号，即总行数
            int rs_num = rs.getRow();
            // 申请响应数量的数组
            back = new String[rs_num + 1];
            // 判断有无数据
            if (rs_num != 0) {
                // TODO return code A0800
                System.out.println("A0800");
                back[0] = "A0800";
                // 把指针指回第一个
                rs.first();
                for (int i = 1; i <= rs_num; ++i) {
                    back[i] = "{[book_id:"+ rs.getString("book_id") +"]," +
                            "[category_name:"+ rs.getString("category_name") +"]," +
                            "[book_name:"+ rs.getString("book_name") +"]," +
                            "[author:"+ rs.getString("author") +"]," +
                            "[press:"+ rs.getString("press") +"]," +
                            "[public_date:"+ rs.getString("public_date")+"]}";
                    rs.next();
                }
                return back;
            } else {
                // TODO return code A0801
                System.out.println("A0801");
                back[0] = "A0801";
                return back;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        back = new String[1];
        // TODO return code A0802
        System.out.println("A0802");
        back[0] = "A0802";
        return back;
    }

    /**
     * @description 预约图书
     * @param bookId 书ID
     * @param userId 用户ID
     * @return String：A09**
     */
    public String orderBook(String bookId, String userId) {

        // 添加预约
        String orderSql = "call orderbook("+ userId +", "+ bookId +", @result);";
        String orderSql1 = "select @result;";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            sql.executeUpdate(orderSql);
            ResultSet rs = sql.executeQuery(orderSql1);
            rs.next();
            if (rs.getBoolean("@result")) {
                sql.closeDataBase();
                // TODO return code A0900
                System.out.println("A0900");
                return "A0900";
            } else {
                sql.closeDataBase();
                // TODO return code A0901
                System.out.println("A0901");
                return "A0901";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            // TODO return code A0902
            System.out.println("A0902");
            return "A0902";
        }
    }

    /**
     * @description 查询历史借阅
     * @param userId 用户ID
     * @return String[]：每个字符串存一个记录
     */
    public String[] checkHistory(String userId) {

        // 返回值
        String[] back;

        // 查询书的信息
        String chkSql = "SELECT * FROM borrowbase WHERE user_id = "+ userId +";";


        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 把指针放到最后
            rs.last();
            // 获取最后一行的行号，即总行数
            int rs_num = rs.getRow();
            // 申请响应数量的数组
            back = new String[rs_num + 1];
            // 判断有无数据
            if (rs_num != 0) {
                // TODO return code A1000
                System.out.println("A1000");
                back[0] = "A1000";
                // 把指针指回第一个
                rs.first();
                for (int i = 1; i <= rs_num; ++i) {
                    back[i] = "{[book_id:"+ rs.getString("book_id") +"]," +
                            "[book_name:"+ rs.getString("book_name") +"]," +
                            "[user_id:"+ rs.getString("user_id") +"]," +
                            "[borrow_date:"+ rs.getString("borrow_date") +"]," +
                            "[return_date:"+ rs.getString("return_date") +"]," +
                            "[is_renew:"+ rs.getString("is_renew") +"]," +
                            "[is_return:"+ rs.getString("is_return") +"]," +
                            "[real_date:"+ rs.getString("real_date")+"]}";
                    rs.next();
                }
            } else {
                // TODO return code A1001
                System.out.println("A1001");
                back[0] = "A1001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            back = new String[1];
            // TODO return code A1002
            System.out.println("A1002");
            back[0] = "A1002";
        }
        return back;
    }

    /**
     * @description 书籍续借
     * @param bookId 书ID
     * @param userId 用户ID
     * @return String 错误码   A11**
     */
    public String renewBook(String bookId, String userId) {

        // 添加预约
        String orderSql = "call renewbook("+ userId +", "+ bookId +", @result);";
        String orderSql1 = "select @result;";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            sql.executeUpdate(orderSql);
            ResultSet rs = sql.executeQuery(orderSql1);

            rs.next();

            if (rs.getBoolean("@result")) {
                sql.closeDataBase();
                // TODO return code A1100
                System.out.println("A1100");
                return "A1100";
            } else {
                sql.closeDataBase();
                // TODO return code A1101
                System.out.println("A1101");
                return "A1101";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            // TODO return code A1102
            System.out.println("A1102");
            return "A1102";
        }
    }

    /**
     * @description 查询个人信息
     * @param userId 用户ID
     * @return String：用户信息
     */
    public String checkUser(String userId) {

        // 返回值
        String back;

        // 查询书的信息
        String chkSql = "SELECT * FROM user WHERE user_id = "+ userId +";";


        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            // 查询结果对象
            ResultSet rs = sql.executeQuery(chkSql);

            // 有数据
            if (rs.next()) {
                back = "{[user_name:"+ rs.getString("user_name") +"]," +
                        "[sex:"+ rs.getString("sex") +"]," +
                        "[age:"+ rs.getString("age") +"]," +
                        "[phone:"+ rs.getString("phone") +"]," +
                        "[num:"+ rs.getString("num") +"]," +
                        "[is_ban:"+ rs.getString("is_ban") +"]," +
                        "[is_manager:"+ rs.getString("is_manager") +"]," +
                        "[phone:"+ rs.getString("phone") +"]}";
                // TODO return code A1200
                System.out.println("A1200");
                return back;
            } else {
                // TODO return code A1201
                System.out.println("A1201");
                return "A1201";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // TODO return code A1202
        System.out.println("A1202");
        return "1202";
    }

    /**
     * @description 更新用户信息
     * @param user_id 用户id
     * @param user_name 用户名
     * @param sex 性别
     * @param age 年龄
     * @param phone 手机号
     * @return String：
     *      成功: "0700"
     *      失败："0701"
     */
    public String updateUser(String user_id, String user_name, String sex, String age, String phone) {

        // 获取Sql查询语句
        String regSql = "UPDATE user SET user_name = '"+user_name+"', sex = '"+sex+"', age = '"+age+"', phone = '"+phone+"' WHERE user_id = '"+user_id+"';";

        // FIXME System.out
        System.out.println(regSql);

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDataBase();
            // TODO return code A1300
            System.out.println("A1300");
            return "A1300";
        }
        sql.closeDataBase();
        // TODO return code A1301
        System.out.println("A1301");
        return "A1301";
    }


    // 管理员功能

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

        // 获取Sql查询语句
        String regSql = "INSERT INTO bookinfo(category_id, book_name, author, price, press, public_date, remain) VALUES ('"+ categoryID +"', '"+ book_name +"', '"+ author +"', '"+ price +"', '"+ press +"', '"+ public_date +"', '"+ remain +"')";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDataBase();
            // TODO return code A1400
            System.out.println("A1400");
            return "A1400";
        }
        sql.closeDataBase();
        // TODO return code A1401
        System.out.println("A1401");
        return "A1401";
    }

    /**
     * @description 下架书籍
     * @param bookId 书籍号
     * @return String：
     *      成功: "0800"
     *      失败："0801"
     */
    public String delBook(String bookId) {

        // 获取Sql查询语句
        String regSql = "DELETE FROM bookinfo WHERE book_id = '"+ bookId +"';";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();
        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDataBase();
            // TODO return code A1500
            System.out.println("A1500");
            return "A1500";
        }
        sql.closeDataBase();
        // TODO return code A1501
        System.out.println("A1501");
        return "A1501";
    }

    /**
     * @description 借书登记
     * @param bookId 书ID
     * @param userId 用户ID
     * @return String[]：每个字符串存一本书的信息
     */
    public String borrowBook(String bookId, String userId) {

        // 添加预约
        String orderSql = "call borrowbook("+ userId +", "+ bookId +", @result);";
        String orderSql1 = "select @result;";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            sql.executeUpdate(orderSql);
            ResultSet rs = sql.executeQuery(orderSql1);
            rs.next();

            if (rs.getBoolean("@result")) {
                sql.closeDataBase();
                // TODO return code A1600
                System.out.println("A1600");
                return "A1600";
            } else {
                sql.closeDataBase();
                // TODO return code A1601
                System.out.println("A1601");
                return "A1601";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sql.closeDataBase();
            // TODO return code A1602
            System.out.println("A1602");
            return "A1602";
        }
    }

    /**
     * @description 归还登记
     * @param bookId 书ID
     * @param userId 用户ID
     * @return String[]：每个字符串存一本书的信息
     */
    public String returnBook(String bookId, String userId) {

        // 添加预约
        String orderSql = "call returnbook("+ userId +", "+ bookId +");";

        // 获取DB对象
        DbManager sql = DbManager.createInstance();
        sql.connectDataBase();

        try {
            sql.executeUpdate(orderSql);
            sql.closeDataBase();
            // TODO return code A1700
            System.out.println("A1700");
            return "A1700";
        } catch (Exception e) {
            e.printStackTrace();
            sql.closeDataBase();
            // TODO return code A1702
            System.out.println("A1702");
            return "A1702";
        }
    }


}