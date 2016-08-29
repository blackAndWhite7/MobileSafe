package com.project.wei.mobilemanager.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;

import com.project.wei.mobilemanager.activity.WatchDogActivity;
import com.project.wei.mobilemanager.dao.WatchDogDao;

import java.util.List;

public class WatchDogService extends Service {

	private WatchDogDao watchDogDao;
	private boolean isLock;
	private UnlockReceiver unlockReceiver;
	private String unlockpackagename;
	private ScreenOffReceiver screenOffReceiver;
	private List<String> list;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//广播接收者，接收解锁界面输入正确密码后，发来的广播
	class UnlockReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			unlockpackagename = intent.getStringExtra("packagename");
		}
	}

	//锁屏的广播接收者，锁屏后重新给应用程序加锁
	class ScreenOffReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			unlockpackagename = null;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		watchDogDao = new WatchDogDao(getApplicationContext());

		//动态注册 UnlockReceiver的广播事件
		unlockReceiver = new UnlockReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.project.wei.mobilemanager.unlock");
		registerReceiver(unlockReceiver,intentFilter);

		//动态注册 ScreenOffReceiver的广播事件
		screenOffReceiver = new ScreenOffReceiver();
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenOffReceiver,intentFilter1);


		//时时刻刻监听用户打开的程序
		//activity都是存放在任务栈中的,一个应用只有一个任务栈
		final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		isLock = true;
		new Thread(){
			public void run() {

				//先将数据库中的数据,查询存放到内存,然后再把数据从内存中获取出来进行操作,
				// 避免每次隔很短时间，就要去查询数据库，执行watchDogDao.queryLockApp(packageName)，
				// 把数据放入list中后，只要判断，当前打开的程序包名在不在这个集合中就可以了

				//当数据库变化的时候重新更新内存中的数据,当数据库变化的时候通知内容观察者数据库变化了,
				//然后在内容观察者中去更新最新的数据
				//notifyForDescendents:匹配规则,true:精确匹配  false:模糊匹配
				getApplicationContext().getContentResolver().registerContentObserver(
						Uri.parse("content://com.project.wei.mobilemanager.lock.changed"),
						true,
						new ContentObserver(null) {
							@Override
							public void onChange(boolean selfChange) {
								list = watchDogDao.queryAllLockApp();
							}
						});
				list = watchDogDao.queryAllLockApp();
				while(isLock){
                     //监听操作
					//监听用户打开了哪些任务栈,打开哪些应用
					//获取正在运行的任务栈,如果任务栈运行,就表示应用打开过
					//maxNum : 获取正在运行的任务栈的个数
					//现在打开的应用的任务栈,永远在第一个,而之前(点击home最小化,没有退出)的应用的任务栈会依次往后推
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					System.out.println("----------------------");
					for (RunningTaskInfo runningTaskInfo : runningTasks) {
						//获取任务栈,栈底的activity
						ComponentName baseactivity = runningTaskInfo.baseActivity;
						/*//获取任务栈栈顶的activity
						runningTaskInfo.topActivity;*/
						String packageName = baseactivity.getPackageName();
						//判断list集合中是否包含包名
						boolean b = list.contains(packageName);
						if (b) {
							if (!packageName.equals(unlockpackagename)) {
								//弹出解锁界面
								Intent intent = new Intent(WatchDogService.this, WatchDogActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("packageName", packageName);
								startActivity(intent);
							}
						}
					}
					SystemClock.sleep(300);
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//服务关闭时禁止监听用户所打开的应用程序
		isLock = false;
		//注销解锁的广播接受者
		if (unlockReceiver != null) {
			unregisterReceiver(unlockReceiver);
			unlockReceiver = null;
		}
		//注销锁屏的广播接受者
		if (screenOffReceiver != null) {
			unregisterReceiver(screenOffReceiver);
			screenOffReceiver = null;
		}
	}
}
