package com.project.wei.mobilemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class AppInfo {
    //名称
    private String name;
    //图标
    private Drawable icon;
    //包名
    private String packagName;
    //版本号
    private String versionName;
    //是否安装到SD卡
    private boolean isSD;
    //是否是用户程序
    private boolean isUser;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public String getPackagName() {
        return packagName;
    }
    public void setPackagName(String packagName) {
        this.packagName = packagName;
    }
    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public boolean isSD() {
        return isSD;
    }
    public void setSD(boolean isSD) {
        this.isSD = isSD;
    }
    public boolean isUser() {
        return isUser;
    }
    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }
    public AppInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    public AppInfo(String name, Drawable icon, String packagName,
                   String versionName, boolean isSD, boolean isUser) {
        super();
        this.name = name;
        this.icon = icon;
        this.packagName = packagName;
        this.versionName = versionName;
        this.isSD = isSD;
        this.isUser = isUser;
    }
    @Override
    public String toString() {
        return "AppInfo [name=" + name + ", icon=" + icon + ", packagName="
                + packagName + ", versionName=" + versionName + ", isSD="
                + isSD + ", isUser=" + isUser + "]";
    }

}
