package com.project.wei.mobilemanager.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class SMSEngine {
    //1.创建刷子
//    回调函数
    public interface ShowProgress {
        //设置最大进度
        public void setMax(int max);
        //设置当前进度
        public void setProgress(int progress);
    }
    //2.媳妇儿给你刷子
    public static void getAllSMS(Context context,ShowProgress showProgress) {
        //1.获取短信
        //1.1获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        //1.2获取内容提供者地址   sms,sms表的地址:null  不写
        //1.3获取查询路径
        //1.4.查询操作
        Cursor query = resolver.query(Uri.parse("content://sms"),
                new String[]{"address", "date", "type", "body"},
                null, null, null);
        //获取短信的个数
        int count = query.getCount();
        //设置最大进度
        showProgress.setMax(count);

        //设置当前进度
        int progress = 0;

        //2.备份短信
        //2.1获取xml序列器
        XmlSerializer xmlSerializer = Xml.newSerializer();
        try {
            //2.2设置xml文件保存的路径
            xmlSerializer.setOutput(new FileOutputStream(new File(context.getFilesDir(), "backupsms.xml")), "utf-8");
            //2.3设置头信息
            xmlSerializer.startDocument("utf-8", true);
            //2.4设置根标签
            xmlSerializer.startTag(null,"smss");
            //1.5.解析cursor
            while (query.moveToNext()) {
                //2.5设置短信的标签
                xmlSerializer.startTag(null,"sms");

                //2.6设置文本内容的标签
                xmlSerializer.startTag(null,"address");
                String address = query.getString(0);
                //2.7设置文本内容
                xmlSerializer.text(address);
                xmlSerializer.endTag(null,"address");

                xmlSerializer.startTag(null,"date");
                String date = query.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null,"date");

                xmlSerializer.startTag(null,"type");
                String type = query.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null,"type");

                xmlSerializer.startTag(null,"body");
                String body = query.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null,"body");

                xmlSerializer.endTag(null,"sms");

                //设置当前进度
                progress++;
                showProgress.setProgress(progress);

            }
            xmlSerializer.endTag(null,"smss");
            xmlSerializer.endDocument();
            //2.8将数据刷新到文件中
            xmlSerializer.flush();
            query.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
