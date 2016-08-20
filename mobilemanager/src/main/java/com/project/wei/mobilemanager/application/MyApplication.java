package com.project.wei.mobilemanager.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class MyApplication extends Application {

    private  static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }
    public static void saveBooleanToSp(String key,boolean b) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key,b);
        edit.commit();
    }
    public static boolean getBooleanFromSp(String key) {
        return  sharedPreferences.getBoolean(key,true);
    }
    public static void saveStringToSp(String key,String name){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,name);
        edit.commit();
    }

    public static String getStringFromSp(String name){
        return sharedPreferences.getString(name,"");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
