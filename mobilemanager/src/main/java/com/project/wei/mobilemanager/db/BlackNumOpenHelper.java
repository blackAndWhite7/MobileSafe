package com.project.wei.mobilemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class BlackNumOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "info";

    public BlackNumOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //第一次创建数据库的调用,创建表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构   字段:   blacknum:黑名单号码     mode:拦截类型
        //参数:创建表结构sql语句
        db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");

    }
    //当数据库版本发生变化的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
