package com.project.wei.mobilemanager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class StreamUtil {
    /*
      将流信息转化成字符串
      这里面的异常，不要用捕获，而是直接抛出，谁用它谁来解决
     */
    public static String parserStreamUtil(InputStream in) throws IOException{
        //字符流,读取流
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        //写入流
        StringWriter sw  = new StringWriter();
        //读写操作
        //数据缓冲区
        String str = null;
        while((str = br.readLine()) !=null){
            //写入操作
            sw.write(str);
        }
        //关流
        sw.close();
        br.close();
        return sw.toString();
    }
}
