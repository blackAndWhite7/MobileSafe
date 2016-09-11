package com.project.wei.mobilemanager.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/14 0014.
 */
public class  ContactEngine {
    public static List<HashMap<String, String>> getAllContactsInfo(Context context) {
        SystemClock.sleep(1000);
        //创建一个ArrayList集合存放查询出的联系人
        ArrayList<HashMap<String ,String >> list  =new ArrayList<HashMap<String ,String >>();

        // 1.获取内容解析者
        ContentResolver contentResolver = context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri uri_raw = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uri_data = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        Cursor cursor = contentResolver.query(uri_raw, new String[]{"contact_id"}, null, null, null);
        // 5.解析cursor
        while (cursor.moveToNext()) {
            // 6.获取查询的数据
            String contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));//等同于 cursor.getString(0);因为就一个字段，多字段的时候为了分清楚可以这样写
            //判断contact_id是否为空,因为你在手机上删除联系人时，只是删除了contact_id 联系人信息还存在，
            // 所以你必须查询contact_id不等于null的，否则会出现空指针异常
            // 空指针: 1.null.方法 2.参数为null
            if (!TextUtils.isEmpty(contact_id)) {   //null   ""
                // 7.根据contact_id查询view_data表中的数据
                Cursor query = contentResolver.query(uri_data, new String[]{"data1","mimetype"},
                        "raw_contact_id=?", new String[]{contact_id}, null);
                //创建一个存放联系人的map
                HashMap<String,String> map = new HashMap<String,String>();
                // 8.解析query
                while (query.moveToNext()) {
                    // 9.获取数据
                    String data1 = query.getString(0);
                    String mimetype = query.getString(1);
                    // 10.根据类型去判断获取的data1数据并保存
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        map.put("phone",data1);
                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                        map.put("name",data1);
                    }

                }
                //不要写在第二个while里面
                // 11.添加到集合中数据
                list.add(map);

                // 12.关闭query
                query.close();
            }
        }
        // 12.关闭cursor
        cursor.close();
        return list;
    }
  /*
    //  一个 white 循环即可解决
  private static List<Map<String,String>> wholeContacts = new ArrayList<Map<String, String>>();

    public static List<Map<String,String>> getAllContacts(Context context){

        Cursor cursor = null;
        try{
            //查询联系人数据
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            // Log.i("haha",cursor.getCount()+"***********");
            while(cursor.moveToNext()){

                String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                //获取联系人姓名
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                //获取联系人手机号
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                //处理删除异常
                if(contact_id!=null) {

                    Map<String, String> contactInfo = new HashMap<String, String>();
                    contactInfo.put("name", displayName);
                    contactInfo.put("phone",number);
                    wholeContacts.add(contactInfo);
                }
            }


        }catch(Exception e){

            e.printStackTrace();
        }finally{

            if(cursor!=null){

                cursor.close();
            }
        }

        return wholeContacts;
    }*/
}
