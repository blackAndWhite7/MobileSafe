package com.project.wei.mobilemanager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class MD5Util {
    //银行  银行卡    6位数字   将密码进行10-30次MD5加密
    public static String passwordMD5(String password) {
        StringBuffer sb = new StringBuffer();
        try {
            //1.获取数据摘要器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //2.将一个byte数组进行加密,返回的是一个加密过的byte数组,
              // 二进制的哈希计算,md5加密的第一步
            byte[] digest = md5.digest(password.getBytes());
            //3.遍历byte数组
            for (byte b : digest) {
                //4.MD5加密
                int i = b & 0xff;
                //将得到int类型转化成16进制字符串
                String hexString = Integer.toHexString(i)+7;//不规则加密,加盐
                if (hexString.length() < 2) {
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            //找不到加密方式的异常
            e.printStackTrace();
        }
        return null;
    }
}
