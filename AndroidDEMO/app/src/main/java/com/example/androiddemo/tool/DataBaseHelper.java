package com.example.androiddemo.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author ghuilrei
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String name = "SQLTest";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;

    private static final String CREATE_TABLE = "create table Session (" +
            "userid char(10) primary key," +
            "session text not null," +
            "ismanager char(5))";

    public DataBaseHelper(Context context) {
        super(context, name, factory, version);
    }

    /** 数据库第一次创建时被调用 **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 软件版本号发生改变时调用
        db.execSQL("drop table if exists Session");
        onCreate(db);
    }
}