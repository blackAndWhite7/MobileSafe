package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;
import com.project.wei.mobilemanager.ui.SettingView;

public class SecondStepActivity extends BaseStepActivity {

    private SettingView sv_secondstep_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step);
        getSupportActionBar().hide();

        sv_secondstep_sim = (SettingView) findViewById(R.id.sv_secondstep_sim);
        //回显操作
        //有保存SIM卡号就是绑定SIM卡,如果没有就是没有绑定SIM卡
        String simSerialNumber_sp = MyApplication.getStringFromSp("simSerialNumber");
        if (!TextUtils.isEmpty(simSerialNumber_sp)) {
            sv_secondstep_sim.setChecked(true);
        } else {
            sv_secondstep_sim.setChecked(false);
        }

        //点击操作
        sv_secondstep_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sv_secondstep_sim.isChecked()) {
                    //解绑
                    MyApplication.saveStringToSp("simSerialNumber","");
                    sv_secondstep_sim.setChecked(false);
                } else {
                    //绑定SIM卡
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    MyApplication.saveStringToSp("simSerialNumber",simSerialNumber);
                    sv_secondstep_sim.setChecked(true);
                }
            }
        });
    }

    @Override
    public void pre_activity() {
        startActivity(new Intent(this, FirstStepActivity.class));
        finish();
    }

    @Override
    public void next_activity() {
        if (sv_secondstep_sim.isChecked()) {
            startActivity(new Intent(this, ThirdStepActivity.class));
            finish();
        } else {
            Toast.makeText(this,"绑定SIM卡才可以继续",Toast.LENGTH_LONG).show();
        }
    }
}
