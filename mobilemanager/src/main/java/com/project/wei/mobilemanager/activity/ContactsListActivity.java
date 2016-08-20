package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.engine.ContactEngine;
import com.project.wei.mobilemanager.utils.MyAsyncTask;

import java.util.HashMap;
import java.util.List;

public class ContactsListActivity extends AppCompatActivity {

    private ListView lv_contactslist_contacts;
    private List<HashMap<String, String>> allContactsInfo;
    //注解初始化控件,类似Spring,注解的形式生成javabean,内部:通过反射的方式执行了findviewById
    // 使用注解的方式初始化控件,在类的成员变量区声明控件,并加上注解
    //.在onCreate方法中初始化操作
    // 优点：：：在控件很多的情况下，不用进行多次的findViewById操作，只需要在onCreate中ViewUtils.inject(this)即可
    //当然，每个控件必须写下面这两条语句，这两条语句要挨着写，
    @ViewInject(R.id.pb_contactslist_plan)
    private ProgressBar progressBar;

    /*  Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          lv_contactslist_contacts.setAdapter(new MyAdaper());
          //数据显示完成,隐藏进度条
          progressBar.setVisibility(View.INVISIBLE);
      }
  };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        getSupportActionBar().hide();
        //初始化
        ViewUtils.inject(this);

        lv_contactslist_contacts = (ListView) findViewById(R.id.lv_contactslist_contacts);
   /*     //解决界面加载数据时,空白页面展示问题,使用进度条
        progressBar.setVisibility(View.VISIBLE);
        //避免在主线程执行耗时操作
        new Thread(){
            @Override
            public void run() {
                allContactsInfo = ContactEngine.getAllContactsInfo(getApplicationContext());
                //获取完联系人的时候给handler发送一个消息,在handler中去setadapter
                handler.sendEmptyMessage(0);
            }
        }.start();*/

        lv_contactslist_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将点击联系人的号码传递给设置安全号码界面
                Intent intent = new Intent();
                intent.putExtra("phone",allContactsInfo.get(position).get("phone"));
                //将数据传递给设置安全号码界面
                //设置结果的方法,会将结果传递给调用当前activity的activity
                setResult(RESULT_OK,intent);
                //移出界面
                finish();
            }
        });
        //注意：这个抽象类要写在onCreate中
        new MyAsyncTask() {
            @Override
            public void preTask() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinBackGround() {
                allContactsInfo = ContactEngine.getAllContactsInfo(getApplicationContext());
            }

            @Override
            public void postTask() {
                lv_contactslist_contacts.setAdapter(new MyAdaper());
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
      /*  //参数:提高扩展性
        //参数1:子线程执行所需的参数
        //参数2:执行的进度
        //参数3:子线程执行的结果
        //异步加载框架:面试必问,手写异步加载框架,百度面试题:当他执行到多少个操作的时候就和new一个线程没区别了,5个
        new AsyncTask<Integer, String, Integer>() {
            //子线程之前执行的方法
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }
            //在子线程之中执行的方法
            @Override
            protected Integer doInBackground(Integer... params) {
                allContactsInfo = ContactEngine.getAllContactsInfo(getApplicationContext());

                return null;
            }
            //在子线程之后执行的方法
            @Override
            protected void onPostExecute(Integer s) {
                lv_contactslist_contacts.setAdapter(new MyAdaper());
                progressBar.setVisibility(View.INVISIBLE);
            }
            //显示当前加载的进度
            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }.execute();*/
    }

    class MyAdaper extends BaseAdapter {

        @Override
        public int getCount() {
            return allContactsInfo.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = View.inflate(getApplicationContext(), R.layout.item_contactslist, null);
            TextView tv_contactslist_name = (TextView) inflate.findViewById(R.id.tv_contactslist_name);
            TextView tv_contactslist_phone = (TextView) inflate.findViewById(R.id.tv_contactslist_phone);
            String name = allContactsInfo.get(position).get("name");
            String phone = allContactsInfo.get(position).get("phone");
            //  Log.i("tag", name + "--------------------" + phone);
            tv_contactslist_name.setText(name);
            tv_contactslist_phone.setText(phone);
            return inflate;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
