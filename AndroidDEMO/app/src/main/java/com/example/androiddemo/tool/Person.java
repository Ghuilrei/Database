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

    /** Session **/
    private String session;

    /** 姓名 **/
    private String name;

    /** 学号 **/
    private String userId;

    /** 身份 **/
    private String isManager;


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
        Cursor cursor = db.query("Session", new String[]{"userid", "session", "ismanager"}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            setUserId(cursor.getString(cursor.getColumnIndex("userid")));
            setSession(cursor.getString(cursor.getColumnIndex("session")));
            setIsManager(cursor.getString(cursor.getColumnIndex("ismanager")));

            // TODO Persion.setAll 的 UserId:
            System.out.println("Persion.setAll 的 UserId:"+getUserId());
        } else {
            setUserId("NULL");
            setSession("NULL");
            setIsManager("NULL");
        }
        setName("NULL");
        cursor.close();
    }

    /**
     * @description 设置Person类中所有信息
     * @param request 格式要求："{[session:666666],[userid:666666],[name:admin],[ismanager:true]}"
     */
    public void setAll (String request, Context context) {
        HashMap<String, String> data = StaticTool.Regular_Expression(request);
        setSession(data.get("session"));
        setUserId(data.get("userId"));
        setIsManager(data.get("ismanager"));
        setName(data.get("name"));

        // TODO Persion.setAll 的 UserId:
        System.out.println("Persion.setAll 的 UserId:"+data.get("userId"));

        DataBaseHelper dbhelper = new DataBaseHelper(context);
        ContentValues values = new ContentValues();
        values.put("userid", getUserId());
        values.put("session", getSession());
        values.put("ismanager", getIsManager());
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

    private void setSession (String session) {
        this.session = session;
    }

    private void setUserId (String userId) {
        this.userId = userId;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setIsManager(String isManager) {
        this.isManager = isManager;
    }

    public String getSession () {
        return session;
    }

    public String getUserId () {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getIsManager() {
        return isManager;
    }

}
