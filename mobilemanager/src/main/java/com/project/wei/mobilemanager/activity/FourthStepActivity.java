package com.project.wei.mobilemanager.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;
import com.project.wei.mobilemanager.receiver.Admin;

public class FourthStepActivity extends BaseStepActivity {

    private CheckBox cb_fourthstep_protect;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_step);
        getSupportActionBar().hide();

        //获取设备的管理者
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //获取超级管理员标示
        componentName = new ComponentName(this, Admin.class);

        cb_fourthstep_protect = (CheckBox) findViewById(R.id.cb_fourthstep_protect);

        //根据保存的用户状态进行回显操作
        if (MyApplication.getBooleanFromSp("protect")) {
            cb_fourthstep_protect.setText("你已经开启了防盗保护");
            cb_fourthstep_protect.setChecked(true);
        } else {
            cb_fourthstep_protect.setText("你还没有开启防盗保护");
            cb_fourthstep_protect.setChecked(false);
        }

        //设置checkbox点击事件
        //当checkbox状态改变的时候调用
        cb_fourthstep_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked :　改变之后的值,点击之后的值
                if (isChecked) {
                    cb_fourthstep_protect.setText("你已经开启了防盗保护");
                    MyApplication.saveBooleanToSp("protect", true);
                } else {
                    cb_fourthstep_protect.setText("你还没有开启防盗保护");
                    MyApplication.saveBooleanToSp("protect",false);
                }
            }
        });
    }

    public void activeAdmin(View view) {
        //代码激活超级管理员
        //设置激活超级管理员
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //设置激活那个超级管理员
        //mDeviceAdminSample : 超级管理员的标示
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        //设置描述信息
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"激活超级管理员！");
        startActivity(intent);
    }

    public void cancleAdmin(View view) {
        //注销超级管理员
        //判断超级管理员是否激活
        if (devicePolicyManager.isAdminActive(componentName)) {
            //注销超级管理员
            devicePolicyManager.removeActiveAdmin(componentName);
        }
    }
    @Override
    public void pre_activity() {
        startActivity(new Intent(this, ThirdStepActivity.class));
        finish();
        //执行平移动画
        //执行界面切换动画的操作,是在startActivity或者finish之后执行
        //enterAnim : 新的界面进入的动画
        //exitAnim : 旧的界面退出的动画
//        overridePendingTransition(R.anim.pre_enter,R.anim.pre_exit);
    }
    @Override
    public void next_activity() {
        MyApplication.saveBooleanToSp("first",false);
        startActivity(new Intent(this,PhoneBakActivity.class));
        finish();
    }
}
