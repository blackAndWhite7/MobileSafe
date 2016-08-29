package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;

public class WatchDogActivity extends AppCompatActivity {

    private ImageView iv_watchdog_icon;
    private TextView tv_watchdog_name;
    private String packagename;
    private EditText ed_watchdog_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_dog);
        getSupportActionBar().hide();
        iv_watchdog_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
        tv_watchdog_name = (TextView) findViewById(R.id.tv_watchdog_name);
        ed_watchdog_password = (EditText) findViewById(R.id.ed_watchdog_password);

        //接受获取数据
        Intent intent = getIntent();
        packagename = intent.getStringExtra("packageName");
        //设置显示加锁的应用程序的图片和名称
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packagename, 0);
            Drawable icon = applicationInfo.loadIcon(pm);
            String name = applicationInfo.loadLabel(pm).toString();
            //设置显示
            iv_watchdog_icon.setImageDrawable(icon);
            tv_watchdog_name.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //解决打开不同被锁了的应用程序时，显示的解锁界面的图标不会改变
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /**
             * Starting: Intent {
             act=android.intent.action.MAIN
             cat=[android.intent.category.HOME
             ] cmp=com.android.launcher/com.android.launcher2.Launcher } from pid 208
             */
            //跳转到主界面
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void lock(View v){
        //解锁
        //1.获取输入的密码
        String password = ed_watchdog_password.getText().toString().trim();
        //2.判断密码是否输入正确
        if ("123".equals(password)) {
            //解锁
            //一般通过广播的形式将信息发送给服务
            Intent intent = new Intent();
            intent.setAction("com.project.wei.mobilemanager.unlock");//自定义发送广播事件
            intent.putExtra("packagename", packagename);
            sendBroadcast(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
        }
    }


}
