package com.project.wei.mobilemanager.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.wei.mobilemanager.R;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class SettingView extends RelativeLayout {
    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private CheckBox cb_setting_update;
    private String des_on;
    private String des_off;
    private String headline;

    //在代码中使用的时候调用
    public SettingView(Context context) {
        super(context);
        //可以在初始化自定义控件的时候就添加控件
        init();
    }
    //在布局文件中使用的时候调用,比两个参数多了个样式
    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    //在布局文件中使用的时候调用
    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        headline = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "headline");
        des_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_on");
        des_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_off");
        tv_setting_title.setText(headline);
        if (isChecked()) {
            tv_setting_des.setText(des_on);
        } else {
            tv_setting_des.setText(des_off);
        }
    }
    /**
     * 添加控件
     */
    private void init(){
        //添加布局文件
        //第一种方式
        //将布局文件转化成view对象
//		View view = View.inflate(getContext(), R.layout.settingview, null);//爹有了,去找孩子,亲生
//		//添加操作
//		this.addView(view);//在自定义组合控件中添加一个textview
        //第二种方式
        //获取view对象,同时给veiw对象设置父控件,相当于先创建一个view对象,在把控件放到自定义控件中
        View view = View.inflate(getContext(), R.layout.settingview, this);//孩子有了,去找爹,喜当爹
        //初始化标题,描述信息,checkbox控件
        //view.findViewById : 表示从settingview布局文件中获取控件
        tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);
        cb_setting_update = (CheckBox) view.findViewById(R.id.cb_setting_update);
    }
    //需要添加一些方法,使程序员能方便的去改变自定义控件中的控件的值

    // 设置标题的方法
//    public void setTitle(String title){
//        tv_setting_title.setText(title);
//    }
    //设置描述信息的方法
//    public void setDes(String des){
//        tv_setting_des.setText(des);
//    }
    //设置checkbox状态
    public void setChecked(boolean isChecked){
        //设置checkbox的状态
        cb_setting_update.setChecked(isChecked);
        if (isChecked) {
            tv_setting_des.setText(des_on);
        } else {
            tv_setting_des.setText(des_off);
        }
    }
    // 获取checkbox的状态
    public boolean isChecked(){
        return cb_setting_update.isChecked();//获取checkbox的状态
    }
}
