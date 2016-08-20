package com.project.wei.mobilemanager.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.project.wei.mobilemanager.service.WidgetService;

public class MyWidget extends AppWidgetProvider {


	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive");
		super.onReceive(context, intent);
	}
	//当更新时间到了,会调用此方法
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		System.out.println("onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//开启更新服务
		Intent intent = new Intent(context,WidgetService.class);
		context.startService(intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		System.out.println("onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		System.out.println("onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		System.out.println("onDisabled");
		super.onDisabled(context);
		//关闭服务
		Intent intent = new Intent(context,WidgetService.class);
		context.stopService(intent);
	}
	
}
