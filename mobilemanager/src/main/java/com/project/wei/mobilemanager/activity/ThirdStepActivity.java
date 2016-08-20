package com.project.wei.mobilemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;

public class ThirdStepActivity extends BaseStepActivity {

    private EditText et_thirdstep_safenumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_step);
        getSupportActionBar().hide();

        et_thirdstep_safenumber = (EditText) findViewById(R.id.et_thirdstep_safenumber);
        //回显在EditText上
        et_thirdstep_safenumber.setText(MyApplication.getStringFromSp("safenumber"));
    }

    public void selectContacts(View view) {
        startActivityForResult(new Intent(this,ContactsListActivity.class),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            et_thirdstep_safenumber.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void pre_activity() {
        startActivity(new Intent(this, SecondStepActivity.class));
        finish();
    }

    @Override
    public void next_activity() {
        String number = et_thirdstep_safenumber.getText().toString().trim();
        if (!TextUtils.isEmpty(number)) {
            MyApplication.saveStringToSp("safenumber", number);
            startActivity(new Intent(this, FourthStepActivity.class));
            finish();
        } else {
            Toast.makeText(this,"输入安全号码才可以继续",Toast.LENGTH_LONG).show();

        }

    }
}
