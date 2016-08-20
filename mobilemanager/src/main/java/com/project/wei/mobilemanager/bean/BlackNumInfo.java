package com.project.wei.mobilemanager.bean;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class BlackNumInfo {
    String blacknum;
    int mode;

    public String getBlacknum() {
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            this.mode = 0;
        }
    }

    public BlackNumInfo() {
    }

    public BlackNumInfo(String blacknum, int mode) {
        this.blacknum = blacknum;
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            this.mode = 0;
        }
    }

    @Override
    public String toString() {
        return "BlackNumInfo{" +
                "blacknum='" + blacknum + '\'' +
                ", mode=" + mode +
                '}';
    }
}
