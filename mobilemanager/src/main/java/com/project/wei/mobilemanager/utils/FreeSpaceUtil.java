package com.project.wei.mobilemanager.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class FreeSpaceUtil {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailSpaceSDCard() {
        //获取SD卡路径
        File file =  Environment.getExternalStorageDirectory();
        //硬盘的API操作
        StatFs statFs = new StatFs(file.getAbsolutePath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return availableBlocksLong * blockSizeLong;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailSpaceROM() {
        //获取ROM路径
        File file =  Environment.getDataDirectory();
        //硬盘的API操作
        StatFs statFs = new StatFs(file.getAbsolutePath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return availableBlocksLong * blockSizeLong;
    }
}
