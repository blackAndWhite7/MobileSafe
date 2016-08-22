package com.project.wei.mobilemanager;

import android.test.AndroidTestCase;
import android.util.Log;

import com.project.wei.mobilemanager.bean.BlackNumInfo;
import com.project.wei.mobilemanager.dao.BlackNumDao;
import com.project.wei.mobilemanager.db.BlackNumOpenHelper;
import com.project.wei.mobilemanager.engine.ContactEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/8/14 0014.
 */
public class MyTestCase extends AndroidTestCase {


    private BlackNumDao blackNumDao;

    //凡是跟业务和数据库相关的都要进行单元测试
    public void testSelectContacts(){
        List<HashMap<String, String>> allContactsInfo = ContactEngine.getAllContactsInfo(getContext());
        for (HashMap<String, String> list : allContactsInfo) {
            String name = list.get("name");
            String phone = list.get("phone");
            Log.i("tag",name+"---------"+phone);
        }
    }
    //在测试方法之前执行的方法
    @Override
    protected void setUp() throws Exception {
        blackNumDao = new BlackNumDao(getContext());
        super.setUp();
    }
    //在测试方法之后执行的方法
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBlackNumOpenHelper(){
        BlackNumOpenHelper blackNumOpenHelper = new BlackNumOpenHelper(getContext(),"blacknum.db",null,1);
        blackNumOpenHelper.getReadableDatabase();
    }
    public void testAddBlackNum(){
        Random random =new Random() ;
        for(int i=0;i<100;i++) {
            blackNumDao.addBlackNum("123456"+i,random.nextInt(3));
        }

    }
    public void testupdateBlackNum(){
        blackNumDao.updateBlackNum("110",BlackNumDao.MODE_SMS);
    }
    public void testqueryBlackNum(){
        int mode = blackNumDao.queryBlackNum("110");
        assertEquals(1,mode);
    }
    public void testdeleteBlackNum(){
        blackNumDao.deleteBlackNum("110");
    }
    public void testqueryAllBlackNum(){
        List<BlackNumInfo> list = blackNumDao.queryAllBlackNum();
        for (BlackNumInfo b : list) {
            Log.i("tag",b.toString());
        }
    }

}
