package com.example.androiddemo.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static String name = "SQLTest";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;

    private static final String CREATE_SESSION = "create table Session (" +
            "sno char(10) primary key," +
            "session text" +
            ");";
    public DBHelper(Context context) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SESSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 软件版本号发生改变时调用
        db.execSQL("drop table if exists Session");
        onCreate(db);
    }
}
