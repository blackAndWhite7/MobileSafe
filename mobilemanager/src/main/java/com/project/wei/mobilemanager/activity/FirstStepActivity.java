package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;

import com.project.wei.mobilemanager.R;

public class FirstStepActivity extends BaseStepActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);
        getSupportActionBar().hide();
    }

    @Override
    public void pre_activity() {}
    @Override
    public void next_activity() {
        // 跳转到第二个界面
        startActivity(new Intent(this, SecondStepActivity.class));
        finish();
//        overridePendingTransition(R.anim.next_enter,R.anim.next_exit);
    }

}
