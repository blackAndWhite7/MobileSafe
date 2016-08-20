package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.service.RocketService;

public class RocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        getSupportActionBar().hide();
    }

    public void startRocket(View view) {
        startService(new Intent(RocketActivity.this, RocketService.class));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
        finish();
    }

    public void endRocket(View view) {
        stopService(new Intent(RocketActivity.this,RocketService.class));
    }
}
