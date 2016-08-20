package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;

public class PhoneBakActivity extends AppCompatActivity {

    private TextView tv_phonebak_phone;
    private ImageView iv_phonebak_protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        boolean first = MyApplication.getBooleanFromSp("first");
        if (first) {
            startActivity(new Intent(this, FirstStepActivity.class));
            finish();//每次跳转时，都要注意，需不需要把前一个界面销毁掉，如果没有销毁，点击返回，就会回到原来的界面
        } else {
            setContentView(R.layout.activity_phone_bak);
            tv_phonebak_phone = (TextView) findViewById(R.id.tv_phonebak_phone);
            iv_phonebak_protect = (ImageView) findViewById(R.id.iv_phonebak_protect);
            //根据保存的安全号码和防盗保护状态进行设置
            tv_phonebak_phone.setText(MyApplication.getStringFromSp("safenumber"));
            if (MyApplication.getBooleanFromSp("protect")) {
                iv_phonebak_protect.setImageResource(R.drawable.lock);
            } else {
                iv_phonebak_protect.setImageResource(R.drawable.unlock);
            }
        }
        //回显设置


    }

    public void refirststep(View view) {
        startActivity(new Intent(this,FirstStepActivity.class));
        finish();
    }
}
