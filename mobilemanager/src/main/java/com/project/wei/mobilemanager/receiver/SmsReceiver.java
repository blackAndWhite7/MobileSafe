package com.project.wei.mobilemanager.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.application.MyApplication;
import com.project.wei.mobilemanager.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {
    //设置一个静态的mediaPlayer，保证每次接收到播放报警音乐指令后，不必重新创建，避免音乐重叠播放
    private static MediaPlayer mediaPlayer;
    public SmsReceiver() {}
    @Override
    //广播接受者在每个接收到一个广播事件,重新new广播接受者
    public void onReceive(Context context, Intent intent) {
        //获取设备的管理者
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        //获取超级管理员标识
        ComponentName componentName = new ComponentName(context,Admin.class);
        //接受解析短信
        //70汉字一条短信,71汉字两条短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for(Object obj:objs) {
            //解析成SmsMessage
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();//获取短信的内容
            String sender = smsMessage.getOriginatingAddress();//获取发件人
            //真机测试,加发件人判断
            if (sender.equals(MyApplication.getStringFromSp("safenumber"))) {
                //判断短信是哪个指令
                if ("#*location*#".equals(body)) {
                    //GPS追踪,开启一个服务，连接GPS是一个耗时操作，不能放在主线程，而且要一直运行，所以用服务来解决
                    Intent intent_gps = new Intent(context, GPSService.class);
                    context.startService(intent_gps);
                    //只当收到指令短信时，拦截短信，让系统收不到
                    abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
                } else if ("#*alarm*#".equals(body)) {
                    //播放报警音乐
                    //在播放报警音乐之前,将系统音量设置成最大
                    //声音的管理者
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    //设置系统音量的大小
                    //streamType : 声音的类型
                    //index : 声音的大小   0最小    15最大
                    //flags : 指定信息的标签
                    //getStreamMaxVolume : 获取系统最大音量,streamType:声音的类型
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                    //判断是否在播放报警音乐，如果已经在播放，就不要去重叠播放，要把正在播放的音乐关掉，重新播放
                    if (mediaPlayer!=null) {
                        mediaPlayer.release();//释放资源
                    }
                    mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                    //设置最大音量和循环播放，这两个方法一般不用了，有时候不好使
                    //mediaPlayer.setVolume(1.0f, 1.0f);
                    //mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                    abortBroadcast();
                } else if ("#*wipedata*#".equals(body)) {
                    //远程删除数据,类似于恢复出厂设置
                    if(devicePolicyManager.isAdminActive(componentName)) {
                        //还是注释掉吧，有点危险
                        //devicePolicyManager.wipeData(0);
                    }
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(body)) {
                    //远程锁屏
                    if(devicePolicyManager.isAdminActive(componentName)) {
                        devicePolicyManager.lockNow();
                    }
                    abortBroadcast();
                }

            }
        }
    }
}
