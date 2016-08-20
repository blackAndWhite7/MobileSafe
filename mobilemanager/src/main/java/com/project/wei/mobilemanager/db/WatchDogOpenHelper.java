package com.project.wei.mobilemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WatchDogOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME="info";
	
	public WatchDogOpenHelper(Context context) {
		super(context, "watchdog.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,packagename varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
