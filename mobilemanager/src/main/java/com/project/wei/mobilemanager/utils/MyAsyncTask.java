package com.project.wei.mobilemanager.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
//异步加载框架，面试必问，要求自己手写出来
public abstract class MyAsyncTask {
    // 在子线程之前执行的方法
    public abstract void preTask();
    //在子线程之中执行的方法
    public abstract void doinBackGround();
    //在子线程之后执行的方法
    public abstract void postTask();

    public void execute(){
        preTask();
        new Thread(){
            @Override
            public void run() {
                doinBackGround();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            postTask();
        }
    };
}
