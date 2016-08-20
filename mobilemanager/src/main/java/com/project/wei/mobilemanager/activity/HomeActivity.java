package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;
import com.project.wei.mobilemanager.utils.MD5Util;

public class HomeActivity extends AppCompatActivity {
    private GridView gv_home_gridview;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new Myadapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position : 条目的位置  0-8
                //根据条目的位置判断用户点击那个条目
                switch (position) {
                    case 0:
                        //手机防盗
                        //跳转到手机防盗模块
                        //判断用户是第一次点击的话设置密码,设置成功再次点击输入密码,密码正确才能进行手机防盗模块
                        String password = MyApplication.getStringFromSp("password");
                        if (TextUtils.isEmpty(password)) {
                            showSetPassWordDialog();
                        } else {
                            showEnterPassWordDialog();
                        }
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this,CallSmsSafeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,SoftManagerActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,TaskManagerActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(HomeActivity.this,RocketActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this,AToolsActivity.class));
                        break;
                    case 8://设置中心
                        Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    boolean status = true;
    private void showEnterPassWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        //显示和隐藏密码
        ImageView iv_enterpassword_show = (ImageView) view.findViewById(R.id.iv_enterpassword_show);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        //点击来改变密码的显示状态
        iv_enterpassword_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    //用代码实现EditText的显示类型，0表示显示（nono），129表示隐藏（textPassword）
                    et_setpassword_password.setInputType(0);
                    status = false;
                } else {
                    et_setpassword_password.setInputType(129);
                    status = true;
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_setpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                String password_sp = MyApplication.getStringFromSp("password");

                if (MD5Util.passwordMD5(password).equals(password_sp)) {
                    startActivity(new Intent(HomeActivity.this,PhoneBakActivity.class));
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
       /*  版本是配的时候用的，可以消除dialog的边框
       alertDialog.setView(view,0,0,0,0);*/
        alertDialog.show();

    }

    private void showSetPassWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        final EditText et_setpassword_confirm = (EditText) view.findViewById(R.id.et_setpassword_confirm);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_setpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                String confrim_password = et_setpassword_confirm.getText().toString().trim();
                if (password.equals(confrim_password)) {
                    MyApplication.saveStringToSp("password", MD5Util.passwordMD5(password));
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private class Myadapter extends BaseAdapter {
        int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
        String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心" };
        // 设置条目的个数
        @Override
        public int getCount() {
            return 9;
        }

        // 设置条目的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //将布局文件转化成view对象
            View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
            //每个条目的样式都不一样,初始化控件,去设置控件的值
            //view.findViewById是从item_home布局文件中找控件,findViewById是从activity_home中找控件
            ImageView iv_itemhome_icon = (ImageView)view.findViewById(R.id.iv_itemhome_icon);
            TextView tv_itemhome_text = (TextView) view.findViewById(R.id.tv_itemhome_text);
            //设置控件的值
            iv_itemhome_icon.setImageResource(imageId[position]);//给imageview设置图片,根据条目的位置从图片数组中获取相应的图片
            tv_itemhome_text.setText(names[position]);
            return view;
        }

        // 获取条目对应的数据
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        // 获取条目的id
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
    }
}
