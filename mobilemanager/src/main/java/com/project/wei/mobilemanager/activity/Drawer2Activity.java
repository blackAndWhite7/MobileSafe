package com.project.wei.mobilemanager.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.project.wei.mobilemanager.R;

public class Drawer2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer2);
        getSupportActionBar().hide();

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.dl);
//		dl.openDrawer(Gravity.RIGHT);//表示默认打开哪个方式布局
//      dl.openDrawer(Gravity.LEFT);
    }
}
