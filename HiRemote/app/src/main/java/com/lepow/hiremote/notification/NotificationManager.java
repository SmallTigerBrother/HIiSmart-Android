package com.lepow.hiremote.notification;

import android.content.Intent;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.notification.TGNotificationManager;

/**
 * 通知管理类
 */
public class NotificationManager extends TGNotificationManager
{
	
private static NotificationManager instanse;
	
	public synchronized static NotificationManager getInstanse()
	{
		if(null == instanse)
		{
			instanse = new NotificationManager();
		}
		return instanse;
	}

	@Override
	protected void onRecieveNotification(Intent intent, int notificationType)
	{
		Intent startIntent = new Intent();
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		switch (notificationType)
		{
		//TODO 添加不同的通知类型，实现跳转逻辑
		default:
			break;
		}

		TGApplication.getInstance().startActivity(startIntent);
	}
}
