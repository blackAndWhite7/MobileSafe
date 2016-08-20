package com.project.wei.mobilemanager.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
//查询Md5值是否在数据库中
public class AntiVirusDao {
	/**
	 * 查询Md5值是否在病毒数据库中
	 * @param context
	 * @param md5
	 * @return
	 */
	public static boolean queryAntiVirus(Context context,String md5){
		boolean ishave = false;
		//1.获取数据库的路径
		File file = new File(context.getFilesDir(), "antivirus.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.query("datable", null, "md5=?", new String[]{md5}, null, null, null);
		if (cursor.moveToNext()) {
			ishave = true;
		}
		cursor.close();
		database.close();
		return ishave;
	}
}
