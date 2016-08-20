package com.project.wei.mobilemanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.project.wei.mobilemanager.R;

public class BackGroundActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_ground);

        ImageView iv_background_smokem = (ImageView) findViewById(R.id.iv_background_smokem);
        ImageView iv_background_smoket = (ImageView) findViewById(R.id.iv_background_smoket);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        iv_background_smokem.startAnimation(alphaAnimation);
        iv_background_smoket.startAnimation(alphaAnimation);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },800);*/
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }.start();

    }
}
