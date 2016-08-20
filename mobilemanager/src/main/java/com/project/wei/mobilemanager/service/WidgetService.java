package com.project.wei.mobilemanager.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.project.wei.mobilemanager.R;
import com.project.wei.mobilemanager.receiver.MyWidget;
import com.project.wei.mobilemanager.utils.TaskUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WidgetService extends Service {


	private AppWidgetManager appWidgetManager;
	private WidgetReceiver widgetReceiver;
	private Timer timer;
	private ScreenOffReceiver screenOffReceiver;
	private ScreenOnReceiver screenOnReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//更新桌面小控件
		//注册清理进程广播接受者
		//1.广播接受者
		widgetReceiver = new WidgetReceiver();
		//2.设置接受的广播事件
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("aa.bb.cc");
		//3.注册广播接受者
		registerReceiver(widgetReceiver, intentFilter);

		//注册锁屏的广播接受者
		screenOffReceiver = new ScreenOffReceiver();
		//设置接受广播事件
		IntentFilter screenoffIntentfilter = new IntentFilter();
		screenoffIntentfilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenOffReceiver, screenoffIntentfilter);

		//注册解锁的广播接受者
		screenOnReceiver = new ScreenOnReceiver();
		IntentFilter screenOnIntentFilter = new IntentFilter();
		screenOnIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(screenOnReceiver, screenOnIntentFilter);

		//widget的管理者
		appWidgetManager = AppWidgetManager.getInstance(this);
		//更新操作
		updateWidgets();
	}
	/**
	 * 清理进程的广播接受者
	 * @author Administrator
	 *
	 */
	private class WidgetReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//清理进程
			killProcess();
		}
	}
	/**
	 * 锁屏的广播接受者
	 * @author Administrator
	 *
	 */
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("锁屏了.....");
			//清理进程
			killProcess();
			//停止更新
			stopUpdates();
		}
	}
	/**
	 * 解锁的广播接受者
	 * @author Administrator
	 *
	 */
	private class ScreenOnReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			updateWidgets();
		}
	}
	/**
	 * 清理进程
	 */
	public void killProcess() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//获取正在运行进程
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			//判断我们的应用进程不能被清理
			if (!runningAppProcessInfo.processName.equals(getPackageName())) {
				am.killBackgroundProcesses(runningAppProcessInfo.processName);
			}
		}
	}

	/**
	 * 停止更新
	 */
	private void stopUpdates() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	/**
	 * 更新widget
	 */
	private void updateWidgets() {
		/*new Thread(){
			public void run() {
				while(true){
					SystemClock.sleep(2000);
				}
			};
		}.start();*/
		//计数器
		timer = new Timer();
		//执行操作
		//task : 要执行操作
		//when : 延迟的时间
		//period : 每次执行的间隔时间
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("widget更新了......");
				//更新操作
				//获取组件的标示
				ComponentName provider = new ComponentName(WidgetService.this, MyWidget.class);
				//获取远程布局
				//packageName : 应用的包名
				//layoutId :widget布局文件
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				//远程布局不能通过findviewbyid获取初始化控件
				//更新布局文件中相应控件的值
				//viewId :更新控件的id
				//text : 更新的内容
				views.setTextViewText(R.id.process_count, "正在运行软件:"+TaskUtil.getProcessCount(WidgetService.this));
				views.setTextViewText(R.id.process_memory, "可用内存:"+Formatter.formatFileSize(WidgetService.this, TaskUtil.getAvailableRam(WidgetService.this)));

				//按钮点击事件
				Intent intent = new Intent();
				intent.setAction("aa.bb.cc");//设置要发送的广播,aa.bb.cc:自定义的广播事件
				//sendBroadcast(intent);
				//通过发送一个广播去表示要执行清理操作,通过接受发送的广播执行清理操作
				//flags : 指定信息的标签
				PendingIntent pendingIntent = PendingIntent.getBroadcast(WidgetService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				//viewId : 点击的控件的id
				//pendingIntent : 延迟意图  包含一个intent意图,当点击的才去执行这个意图,不点击就不执行意图
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				//更新操作
				appWidgetManager.updateAppWidget(provider, views);
			}
		}, 2000, 2000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//停止更新widget
		stopUpdates();
		//注销清理进程的广播接受者
		if (widgetReceiver != null) {
			unregisterReceiver(widgetReceiver);
			widgetReceiver = null;
		}
		if (screenOffReceiver != null) {
			unregisterReceiver(screenOffReceiver);
			screenOffReceiver = null;
		}
		if (screenOnReceiver != null) {
			unregisterReceiver(screenOnReceiver);
			screenOnReceiver = null;
		}
	}


}
