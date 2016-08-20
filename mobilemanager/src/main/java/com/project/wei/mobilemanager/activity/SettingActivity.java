package com.project.wei.mobilemanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;
import com.project.wei.mobilemanager.service.AddressService;
import com.project.wei.mobilemanager.service.BlackNumService;
import com.project.wei.mobilemanager.service.WatchDogService;
import com.project.wei.mobilemanager.ui.SettingClickView;
import com.project.wei.mobilemanager.ui.SettingView;
import com.project.wei.mobilemanager.utils.AddressUtils;

public class SettingActivity extends AppCompatActivity {
    private SettingView sv_setting_update;
    private SettingView sv_setting_address;
    private SettingView sv_setting_blacknum;
    private SettingView sv_setting_watchdog;
    private SettingClickView scv_setting_changedbg;
    private SettingClickView scv_setting_location;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        //name : 保存信息的文件的名称
        //mode : 权限
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
        sv_setting_blacknum = (SettingView) findViewById(R.id.sv_setting_blacknum);
        scv_setting_changedbg = (SettingClickView) findViewById(R.id.scv_setting_changedbg);
        scv_setting_location = (SettingClickView) findViewById(R.id.scv_setting_location);
        sv_setting_watchdog = (SettingView) findViewById(R.id.sv_setting_watchdog);
        update();
        changedbg();
        location();

    }

    /**
     * 归属地提示框位置
     */
    private void location() {
        scv_setting_location.setTitle("归属地提示框位置");
        scv_setting_location.setDes("设置归属地提示框的显示位置");
        scv_setting_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //跳转到设置位置的界面
                Intent intent = new Intent(SettingActivity.this,DragViewActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置归属地提示框风格
     */
    private void changedbg() {
        final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
        //设置标题和描述信息
        scv_setting_changedbg.setTitle("归属地提示框风格");
        //根据保存的选中的选项的索引值设置自定义组合控件描述信息回显操作
        scv_setting_changedbg.setDes(items[sp.getInt("which", 0)]);
        //设置自定义组合控件的点击事件
        scv_setting_changedbg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //弹出单选对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                //设置图标
                builder.setIcon(R.drawable.ic_launcher);
                //设置标题
                builder.setTitle("归属地提示框风格");
                //设置单选框
                //items : 选项的文本的数组
                //checkedItem : 选中的选项
                //listener : 点击事件
                //设置单选框选中选项的回显操作
                builder.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener(){
                    //which : 选中的选项索引值
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("which", which);
                        edit.commit();
                        //1.设置自定义组合控件描述信息文本
                        scv_setting_changedbg.setDes(items[which]);//根据选中选项索引值从items数组中获取出相应文本,设置给描述信息控件
                        //2.隐藏对话框
                        dialog.dismiss();
                    }
                });
                //设置取消按钮
                builder.setNegativeButton("取消", null);//当点击按钮只是需要进行隐藏对话框的操作的话,参数2可以写null,表示隐藏对话框
                builder.show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        address();
        blackNum();
        lock();
    }

    private void lock() {
        //回显操作
        //动态的获取服务是否开启
        if (AddressUtils.isRunningService("com.project.wei.mobilemanager.service.WatchDogService", getApplicationContext())) {
            //开启服务
            sv_setting_watchdog.setChecked(true);
        }else{
            //关闭服务
            sv_setting_watchdog.setChecked(false);
        }
        sv_setting_watchdog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,WatchDogService.class);
                //根据checkbox的状态设置描述信息的状态
                //isChecked() : 之前的状态
                if (sv_setting_watchdog.isChecked()) {
                    //关闭提示更新
                    stopService(intent);
                    //更新checkbox的状态
                    sv_setting_watchdog.setChecked(false);
                }else{
                    //打开提示更新
                    startService(intent);
                    sv_setting_watchdog.setChecked(true);
                }
            }
        });
    }

    private void blackNum() {
        //回显操作
                //动态的获取服务是否开启
                if (AddressUtils.isRunningService("com.project.wei.mobilemanager.service.BlackNumService", getApplicationContext())) {
                    //开启服务
                    sv_setting_blacknum.setChecked(true);
                }else{
                    //关闭服务
                    sv_setting_blacknum.setChecked(false);
                }
                sv_setting_blacknum.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingActivity.this,BlackNumService.class);
                        //根据checkbox的状态设置描述信息的状态
                        //isChecked() : 之前的状态
                        if (sv_setting_blacknum.isChecked()) {
                            //关闭提示更新
                            stopService(intent);
                            //更新checkbox的状态
                            sv_setting_blacknum.setChecked(false);
                        }else{
                            //打开提示更新
                            startService(intent);
                            sv_setting_blacknum.setChecked(true);
                        }
            }
        });
    }

    private void address() {
        //回显操作
        //动态的获取服务是否开启
        if (AddressUtils.isRunningService("com.project.wei.mobilemanager.service.AddressService", getApplicationContext())) {
            //开启服务
            sv_setting_address.setChecked(true);
        }else{
            //关闭服务
            sv_setting_address.setChecked(false);
        }
        sv_setting_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AddressService.class);
                //根据checkbox的状态设置描述信息的状态
                //isChecked() : 之前的状态
                if (sv_setting_address.isChecked()) {
                    //关闭提示更新
                    stopService(intent);
                    //更新checkbox的状态
                    sv_setting_address.setChecked(false);
                }else{
                    //打开提示更新
                    startService(intent);
                    sv_setting_address.setChecked(true);
                }
            }
        });
    }

    private void update() {
        //初始化自定义控件中各个控件的值
        // sv_setting_update.setTitle("提示更新");
        //defValue : 缺省的值
        if (MyApplication.getBooleanFromSp("update")) {
          //  sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else{
          //  sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }
        //设置自定义组合控件的点击事件
        //问题1:点击checkbox发现描述信息没有改变,原因:因为checkbox天生是有点击事件和获取焦点事件,当点击checkbox,这个checkbox就会执行他的点击事件
        //而不会执行条目的点击事件
        //问题2:没有保存用户点击操作
        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (sv_setting_update.isChecked()) {
                    //关闭提示更新
                    sv_setting_update.setChecked(false);
                    //保存状态
                    MyApplication.saveBooleanToSp("update", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                }else{
                    //打开提示更新
                    sv_setting_update.setChecked(true);
                    //保存状态
                    MyApplication.saveBooleanToSp("update", true);
                }
            }
        });
    }
}
