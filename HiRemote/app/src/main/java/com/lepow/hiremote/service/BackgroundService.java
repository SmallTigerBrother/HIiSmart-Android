package com.lepow.hiremote.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 后台运行的Service，随应用启动；连接蓝牙设备后，服务激活
 * 实现定位、录音等功能
 * @author peng
 *
 */
public class BackgroundService extends Service
{
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}
}
