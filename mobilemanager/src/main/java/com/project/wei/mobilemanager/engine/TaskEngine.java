package com.project.wei.mobilemanager.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.project.wei.mobilemanager.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class TaskEngine {
    /**
     * 获取系统中所有进程信息
     * @return
     */
    public static List<TaskInfo> getTaskAllInfo(Context context){
        List<TaskInfo> list = new ArrayList<TaskInfo>();
        //1.进程的管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        //2.获取所有正在运行的进程信息
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        //遍历集合
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            //3.获取相应的信息
            //获取进程的名称,获取包名
            String packagName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(packagName);
            //获取进程所占的内存空间,int[] pids : 输入几个进程的pid,就会返回几个进程所占的空间
            Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
            int totalPss = memoryInfo[0].getTotalPss();
            long ramSize = totalPss*1024;
            taskInfo.setRamSize(ramSize);
            try {
                //获取application信息
                //packageName : 包名     flags:指定信息标签
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packagName, 0);
                //获取图标
                Drawable icon = applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);
                //获取名称
                String name = applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);
                //获取程序的所有标签信息,是否是系统程序是以标签的形式展示
                int flags = applicationInfo.flags;
                boolean isUser;
                //判断是否是用户程序
                if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
                    //系统程序
                    isUser = false;
                }else{
                    //用户程序
                    isUser = true;
                }
                //保存信息
                taskInfo.setUser(isUser);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //taskinfo添加到集合
            list.add(taskInfo);
        }
        return list;
    }

}
