package com.project.wei.mobilemanager.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.engine.SMSEngine;

public class AToolsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        getSupportActionBar().hide();
    }

    public void query(View view) {
        startActivity(new Intent(this,AddressActivity.class));
    }

    public void backupSms(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        new Thread(){
            @Override
            public void run() {
//                3.我们接受刷子,进行刷牙,刷鞋,刷马桶.....
                SMSEngine.getAllSMS(getApplicationContext(), new SMSEngine.ShowProgress() {
                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                    }
                    @Override
                    public void setProgress(int progress) {
                        progressDialog.setProgress(progress);
                    }
                });
                progressDialog.dismiss();
            }
        }.start();
    }

    public void restoreSms(View view) {
        /*//解析xml  new File(getFilesDir(), "backupsms.xml")
        XmlPullParser xmlPullParser = Xml.newPullParser();
        try {
            FileInputStream fileInputStream = new FileInputStream("/data/data/"+getPackageName()+"/files/backupsms.xml");
            xmlPullParser.setInput(fileInputStream,"utf-8");
            int next = xmlPullParser.next();
            while (next != xmlPullParser.END_DOCUMENT) {
                switch (next) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String startTagName = xmlPullParser.getName();
                        Log.i("taggggg",startTagName);
                        if (startTagName.equals("address")) {
                            String addresss = xmlPullParser.nextText();
                            Log.i("taggggg",addresss);
                        } else if (startTagName.equals("date")) {
                            String date = xmlPullParser.nextText();
                            Log.i("taggggg",date);
                        }else if (startTagName.equals("type")) {
                            String type = xmlPullParser.nextText();
                            Log.i("taggggg",type);
                        }else if (startTagName.equals("body")) {
                            String body = xmlPullParser.nextText();
                            Log.i("taggggg",body);
                        }
                        break;


                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //插入短信
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://sms");
        ContentValues values = new ContentValues();
        values.put("address", "95588");
        values.put("date", System.currentTimeMillis());
        values.put("type", 1);
        values.put("body", "zhuan zhang le $10000000000000000000");
        resolver.insert(uri, values);
    }

    public void expandablelistview(View view) {
        startActivity(new Intent(this,ExpandableListViewActivity.class));
    }

    public void drawer1(View view) {
        startActivity(new Intent(this,DrawerActivity.class));
    }
    public void drawer2(View view) {
        startActivity(new Intent(this,Drawer2Activity.class));
    }
}
