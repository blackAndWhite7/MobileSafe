package com.project.wei.mobilemanager.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class HomeTextView extends TextView{
    //在代码中使用时调用
    public HomeTextView(Context context) {
        super(context);
    }
    //在布局文件中使用的时候调用
    //布局文件中的控件都是可以用代码来表示
    //AttributeSet : 保存了控件在布局文件中的所有属性
    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //在布局文件中使用的时候调用,比两个参数多了样式
    public HomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //设置自定义的textview自动获取焦点
    //是否获取焦点
    @Override
    public boolean isFocused() {
        //true:获取焦点,false:不获取焦点
        return true;
    }
}
