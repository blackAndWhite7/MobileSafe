package com.project.wei.mobilemanager.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
//异常捕获
//Application : 当前的应用程序,所有的应用最先的执行都是applicaiton
public class MyApplication extends Application {

    private  static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        //处理可能发生的异常
//        Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
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

 /*   private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        //系统中由未捕获的异常的时候调用
        //Throwable : Error和Exception的父类
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            System.out.println("哥捕获异常了......");
            ex.printStackTrace();
            try {
                //将捕获到异常,保存到文件中
                ex.printStackTrace(new PrintStream(new File(getFilesDir(),"log.txt")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //myPid() : 获取当前应用程序的进程id
            //自己把自己杀死
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }*/

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
