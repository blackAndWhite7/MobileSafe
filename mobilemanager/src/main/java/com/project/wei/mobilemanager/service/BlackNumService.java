package com.project.wei.mobilemanager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.project.wei.mobilemanager.dao.BlackNumDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlackNumService extends Service {

    private MySmsReceiver mySmsReceiver;
    private BlackNumDao blackNumDao;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;

    public BlackNumService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //先获取访问数据库的对象
        blackNumDao = new BlackNumDao(getApplicationContext());
        //代码注册短信到来的广播接受者，短信拦截是通过广播接收者来实现的，电话拦截是通过设置监听事件实现的
        //1.创建广播接受者
        mySmsReceiver = new MySmsReceiver();
        //2.设置监听广播事件
        IntentFilter intentFilter = new IntentFilter();
        //广播接受者的最高优先级Integer.MAX_VALUE,优先级相同,代码注册的广播接受者先接受广播事件
        intentFilter.setPriority(Integer.MAX_VALUE);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //3.注册广播接受者
        registerReceiver(mySmsReceiver,intentFilter);
        //监听电话状态
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        //参数1:监听
        //参数2:监听的事件
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
   // 拦截短信的广播接收者
    class MySmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接受解析短信
            //70汉字一条短信,71汉字两条短信
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs) {
                //解析成SmsMessage
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();//获取发件人
                //根据发件人号码,获取号码的拦截模式
                int mode = blackNumDao.queryBlackNum(sender);
                //判断是否是短信拦截或者是全部拦截
                if (mode == BlackNumDao.MODE_SMS || mode == BlackNumDao.MODE_ALL) {
                    abortBroadcast();//拦截广播事件,拦截短信操作
                }

            }
        }
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //如果是响铃状态,检测拦截模式是否是电话拦截,是挂断
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //获取拦截模式
                int mode = blackNumDao.queryBlackNum(incomingNumber);
                if (mode == BlackNumDao.MODE_CALL || mode == BlackNumDao.MODE_ALL) {
                    //挂断电话
                    endCall();

                    //删除通话记录
                    //1.获取内容解析者
                     final ContentResolver resolver = getContentResolver();
                    //2.获取内容提供者地址  call_log   calls表的地址:calls
                    //3.获取执行操作路径
                     final Uri uri = Uri.parse("content://call_log/calls");
                    //4.删除操作 这个方法是删除以前的通话记录，而刚打进来的电话还会加入到通话记录中
                    //resolver.delete(uri, "number=?", new String[]{incomingNumber});
                    //通过内容观察者观察内容提供者内容,如果变化,就去执行删除操作
                    //notifyForDescendents : 匹配规则,true : 精确匹配   false:模糊匹配
                    resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        //内容提供者内容变化的时候调用
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            //删除通话记录
                            resolver.delete(uri, "number=?", new String[]{incomingNumber});
                            //注销内容观察者
                            resolver.unregisterContentObserver(this);
                        }
                    });

                }
            }
        }
    }

    private void endCall() {
        //通过反射进行实现
        try {
            //1.通过类加载器加载相应类的class文件
            // Class<?> aClass = Class.forName("android.os.ServiceManager");
            Class<?> loadClass = BlackNumService.class.getClassLoader().loadClass("android.os.ServiceManager");
            //2.获取类中相应的方法
            //name : 方法名
            //parameterTypes : 参数类型
            Method method = loadClass.getDeclaredMethod("getService", String.class);
            //3.执行方法,获取返回值
            //receiver : 类的实例
            //args : 具体的参数
            IBinder invoke = (IBinder) method.invoke(null, Context.TELECOM_SERVICE);
            //aidl
            ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
            //挂断电话
            iTelephony.endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        //注销短信的广播接受者
        unregisterReceiver(mySmsReceiver);
        //取消来电监听
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
    }
}
