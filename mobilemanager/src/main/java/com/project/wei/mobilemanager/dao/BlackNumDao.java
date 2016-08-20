package com.project.wei.mobilemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.wei.mobilemanager.bean.BlackNumInfo;
import com.project.wei.mobilemanager.db.BlackNumOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class BlackNumDao {
    public static final int MODE_CALL = 0;
    public static final int MODE_SMS = 1;
    public static final int MODE_ALL = 2;
    private BlackNumOpenHelper blackNumOpenHelper;

    //在构造函数中获取BlackNumOpenHlper
    public BlackNumDao(Context context){
        blackNumOpenHelper = new BlackNumOpenHelper(context,"blacknum.db",null,1);
    }

    //添加黑名单
    public  void addBlackNum(String blacknum,int mode){
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("blacknum",blacknum);
        values.put("mode",mode);
        database.insert(BlackNumOpenHelper.DB_NAME,null,values);
        database.close();
    }

    // 更新黑名单的拦截模式
    public void updateBlackNum(String blacknum,int mode) {
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        database.update(BlackNumOpenHelper.DB_NAME,values,"blacknum = ?",new String[]{blacknum});
        database.close();
    }

    //通过黑名单号码,查询黑名单号码的拦截模式
    public int queryBlackNum(String blacknum){
        int MODE = -1;
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        Cursor query = database.query(BlackNumOpenHelper.DB_NAME, new String[]{"mode"}, "blacknum = ?",
                new String[]{blacknum}, null, null, null);
        if (query.moveToNext()) {
            MODE = query.getInt(0);
        }
        query.close();
        database.close();
        return MODE;
    }

    // 根据黑名单号码,删除相应的数据
    public void deleteBlackNum(String blacknum) {
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        database.delete(BlackNumOpenHelper.DB_NAME,"blacknum = ?",new String[]{blacknum});
        database.close();
    }
    public List<BlackNumInfo> queryAllBlackNum(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        Cursor query = database.query(BlackNumOpenHelper.DB_NAME, new String[]{"blacknum", "mode"}, null, null, null, null, "_id desc");
        while (query.moveToNext()) {
            String blacknum = query.getString(0);
            int mode = query.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        query.close();
        database.close();
        return list;
    }
    /*
      查询部分数据
      查询20条数据
      MaxNum : 查询的总个数
      startindex : 查询的起始位置*/
    /*
        select blacknum from info order by _id desc limit 20 offset 20
        limit : 查询的总个数  offset :查询的起始位置
        select blacknum from info order by _id desc limit 0,20
        前面:查询的起始位置   后面:查询的总个数
	*/
    public List<BlackNumInfo> queryPartBlackNum(int partnum,int startindex){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        Cursor query = database.rawQuery("select blacknum,mode from info order by _id desc limit ?,? ",
                new String[]{ startindex + "",partnum+ ""});
        while (query.moveToNext()) {
            String blacknum = query.getString(0);
            int mode = query.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        query.close();
        database.close();
        return list;
    }
}
