package com.project.wei.mobilemanager.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.activity.BackGroundActivity;

public class RocketService extends Service {

    private WindowManager windowManager;
    private View view;
    private ImageView iv_rocket;
    private WindowManager.LayoutParams params;
    private int width;
    private int height;

    public RocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        view = View.inflate(getApplicationContext(), R.layout.rocket, null);
        iv_rocket = (ImageView) view.findViewById(R.id.iv_rocket);
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        AnimationDrawable animationDrawable = (AnimationDrawable) iv_rocket.getBackground();
        animationDrawable.start();
        windowManager.addView(view, params);
        setTouch();
    }

    private void setTouch() {
        iv_rocket.setOnTouchListener(new View.OnTouchListener() {

            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int  newX = (int) event.getRawX();
                        int  newY = (int) event.getRawY();

                        int dx = newX - startX;
                        int dy = newY - startY;
                        params.x = params.x +dx;
                        params.y = params.y +dy;
                        if (params.x < 0) {
                            params.x =0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > width - view.getWidth()) {
                            params.x = width - view.getWidth();
                        }
                        if (params.y > height - view.getHeight() - 30) {
                            params.y = height - view.getHeight() -30;
                        }
                        windowManager.updateViewLayout(view,params);
                        startX = newX;
                        startY = newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (params.y>300) {
                            sendRocket();
                            Intent intent = new Intent(RocketService.this, BackGroundActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            params.y-=20;
            windowManager.updateViewLayout(view,params);
        }
    };

    private void sendRocket() {
        new Thread(){
            @Override
            public void run() {
                for (int i = 0;i<50;i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && view != null) {
            windowManager.removeView(view);
            windowManager = null;
            view = null;
        }
    }
}
