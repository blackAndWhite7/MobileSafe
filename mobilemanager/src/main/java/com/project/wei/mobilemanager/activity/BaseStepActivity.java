package com.project.wei.mobilemanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public abstract class BaseStepActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
/*记住！！！重写onCreate方法时一定要记得时一个参数的，而且类型时protected*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.获取手势识别器
        gestureDetector = new GestureDetector(this,new MyOnGestureListener());
    }
    //界面的触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //要想让手势是识别器生效,必须将手势识别器注册到屏幕的触摸事件中
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //e1 : 按下的事件,保存按下的坐标
            //e2 : 抬起的事件,保存抬起的坐标
            //velocityX : velocity 速度    在x轴上移动的速率

            float startX = e1.getRawX();
            float endX = e2.getRawX();
            float startY = e1.getRawY();
            float endY = e2.getRawY();
            //判断是否是斜滑  斜滑超过100像素就不会反应
            if (Math.abs(endY - startY) > 150) {
                Toast.makeText(getApplicationContext(), "好好滑，不要这个样子惹！", Toast.LENGTH_LONG).show();
                //拦截事件
                return false;
            }
            if (startX - endX > 100) {
                next_activity();
                overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
            }
            if (endX - startX > 100) {
                pre_activity();
                overridePendingTransition(R.anim.pre_enter,R.anim.pre_exit);
            }
            //true if the event is consumed, else false
            //true : 事件执行     false:拦截事件,事件不执行
            return true;
//            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    //  因为button的点击事件是 next ,当你点击button时，会调用它父类中的这个方法，
//  并不是直接调用next_activity这个方法，子类中只需要实现抽象的方法即可
    public void next(View view){
        next_activity();
        //要在每一个pre  next 设置活动效果
        overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
    }
    public void pre(View view){
        pre_activity();
        overridePendingTransition(R.anim.pre_enter,R.anim.pre_exit);
    }
    // 因为父类不知道子类上一步,下一步具体的执行操作代码,所以要创建一个抽象方法,
    // 让子类实现这个抽象方法,根据自己的特性去实现相应的操作
    public abstract void pre_activity();
    public abstract void next_activity();
    /*
    第二个抽取:因为在进行第二三四个界面的时候,点击返回键会直接回到主界面,因为每个界面中都有返回键操作,
      所以向上抽取到父类进行统一的处理,子类就不去单独处理返回键的操作
      */
    @Override
    //监听手机物理按钮的点击事件
    //keyCode :　物理按钮的标示
    //event : 按键的处理事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断keycode是否是返回键的标示
        if (keyCode == event.KEYCODE_BACK) {
            pre_activity();
            overridePendingTransition(R.anim.pre_enter,R.anim.pre_exit);
            //true:是可以屏蔽按键的事件，就是你按键没有反应，
            // 在高版本中，event.KEYCODE_HOME不可以被屏蔽
            //return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
