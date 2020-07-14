package com.example.androiddemo.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.io.Serializable;
import java.util.HashMap;

/**
 * @description 个人信息类
 * @author Ghuilrei
 * @date 2020/7/9 21:04
 * @version V1.0
 */

public class Person implements Serializable {


    /** 姓名 **/
    private String user_name;

    /** 学号 **/
    private String user_id;

    /** Session **/
    private String session;

    /** 手机号 **/
    private String phone;

    /** 身份 **/
    private String is_manager;

    /** 账号状态 **/
    private String is_ban;


    /**
     * @description 构造函数
     * @param context 上下文
     */
    public Person(Context context) {

        DataBaseHelper dbhelper = new DataBaseHelper(context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query("Session", new String[]{"user_name", "user_id", "session", "phone", "is_manager", "is_ban"}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            // 数据库里有信息
            setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
            setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
            setSession(cursor.getString(cursor.getColumnIndex("session")));
            setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            setIs_manager(cursor.getString(cursor.getColumnIndex("is_manager")));
            setIs_ban(cursor.getString(cursor.getColumnIndex("is_ban")));

        } else {
            // 数据库里没信息
            setUser_name("NULL");
            setUser_id("NULL");
            setSession("NULL");
            setPhone("NULL");
            setIs_manager("NULL");
            setIs_ban("NULL");
        }
        cursor.close();
    }

    /**
     * @description 设置Person类中所有信息
     * @param data 格式要求："{[session:666666],[userid:666666],[name:admin],[ismanager:true]}"
     */
    public void setAll (HashMap<String, String> data, Context context) {

        // 从传入数据中提取个人信息
        setUser_name(data.get("user_name"));
        setUser_id(data.get("user_id"));
        setSession(data.get("session"));
        System.out.println("123123123123123123123:"+data.get("session"));
        setPhone(data.get("phone"));
        setIs_manager(data.get("is_manager"));
        setIs_ban(data.get("is_ban"));

        DataBaseHelper dbhelper = new DataBaseHelper(context);
        ContentValues values = new ContentValues();

        values.put("user_name", getUser_name());
        values.put("user_id", getUser_id());
        values.put("session", getSession());
        values.put("phone", getPhone());
        values.put("is_manager", getIs_manager());
        values.put("is_ban", getIs_ban());

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        if (db.insert("Session", null, values) == -1) {
            // TODO Session 插入失败
            System.out.println("Session 插入失败");
            if (db.update("Session", values, "rowid=?", new String[]{"1"}) == -1) {
                // TODO Session 更新失败
                System.out.println("Session 更新失败");
            } else {
                // TODO Session 更新成功
                System.out.println("Session 更新成功");
            }
        }
        // TODO Session 插入成功
        System.out.println("Session 插入成功");
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIs_manager(String is_manager) {
        this.is_manager = is_manager;
    }

    public void setIs_ban(String is_ban) {
        this.is_ban = is_ban;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSession() {
        return session;
    }

    public String getPhone() {
        return phone;
    }

    public String getIs_manager() {
        return is_manager;
    }

    public String getIs_ban() {
        return is_ban;
    }
}

