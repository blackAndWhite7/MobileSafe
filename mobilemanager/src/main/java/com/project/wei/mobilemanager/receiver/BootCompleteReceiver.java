package com.project.wei.mobilemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.project.wei.mobilemanager.application.MyApplication;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        //开启防盗保护才会执行以下操作
        if (MyApplication.getBooleanFromSp("protect")) {
            //检查SIM卡是否发生变化
            //1.获取保存的SIM卡号
            String simSerialNumber_sp = MyApplication.getStringFromSp("simSerialNumber");
            //2.再次获取本地SIM卡号
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            //telephonyManager.getLine1Number();//获取SIM卡绑定的电话号码    line1:双卡双待.在中国不太适用,运营商一般不会将SIM卡和手机号码绑定
            // 获取SIM卡序列号,唯一标示
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            //3.判断两个SIM卡号是否为空
            if (!TextUtils.isEmpty(simSerialNumber) && !TextUtils.isEmpty(simSerialNumber_sp)) {
                //4.判断两个SIM卡是否一致,如果一致就不发送报警短信,不一致发送报警短信
                if (!simSerialNumber.equals(simSerialNumber_sp)) {
                    //发送报警短信
                    //短信的管理者
                    SmsManager smsManager = SmsManager.getDefault();
                    //destinationAddress : 收件人
                    //scAddress :　短信中心的地址　　一般null
                    //text : 短信的内容
                    //sentIntent : 是否发送成功
                    //deliveryIntent : 短信的协议  一般null
                    smsManager.sendTextMessage(MyApplication.getStringFromSp("phone"), null, "mdzz!", null, null);
                }
            }
        }
    }
}
