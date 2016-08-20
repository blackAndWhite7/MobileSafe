package com.project.wei.mobilemanager.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.dao.AddressDao;

public class AddressActivity extends AppCompatActivity {

    @ViewInject(R.id.et_address_phonenumber)
    private EditText et_address_phonenumber;
    @ViewInject(R.id.tv_address_show)
    private TextView tv_address_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getSupportActionBar().hide();
        ViewUtils.inject(this);
        //监听输入框文本变化
        et_address_phonenumber.addTextChangedListener(new TextWatcher() {
            //当文本变化之前调用
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //当文本变化完成的的时候调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //1.获取输入框输入的内容
                String phone = s.toString();
                //2.根据号码查询号码归属地
                String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
                //3.判断查询的号码归属地是否为空
                if (!TextUtils.isEmpty(queryAddress)) {
                    //将查询的号码归属地设置给textveiw显示
                    tv_address_show.setText(queryAddress);
                }
            }
            //文本变化之后调用
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void queryLocation(View view) {
        //1.获取输入的号码
        String phone = et_address_phonenumber.getText().toString().trim();
        //2.判断号码是否为空
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_LONG).show();

            //实现抖动的效果
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            /*shake.setInterpolator(new Interpolator() {
            @Override
               public float getInterpolation(float x) {
                 return 0;//根据x的值获取y的值  y=x*x  y=x-k
               }
            });*/
            et_address_phonenumber.startAnimation(shake);//开启动画.
            //1.获取振动的管理者
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //2.振动的效果
            //vibrator.vibrate(Long.MAX_VALUE);//milliseconds : 振动的持续时间,毫秒值,但是在国产定制系统只会振动一次,比如小米
            //pattern : 振动平率
            //repeat : 是否重复振动,-1不重复,非-1重复,如果是非-1表示从振动平率的那个元素开始振动
            vibrator.vibrate(new long[]{20l,10l,20l,10l}, -1);
        }else{
            // 3.根据号码查询号码归属地
            String location = AddressDao.queryAddress(phone, getApplicationContext());
            //4.判断查询的号码归属地是否为空
            if (!TextUtils.isEmpty(location)) {
                tv_address_show.setText(location);
            }
        }
    }
}
