package com.lepow.hiremote.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 通知栏通知点击事件统一接受广播类
 */
public class NotificationBroadCastReciever extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		//委托给PBNotificationManager处理
		NotificationManager.getInstanse().recieveNotification(intent);
	}
}
