package com.lepow.hiremote.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.lepow.hiremote.app.HSApplication;
import com.lepow.hiremote.bluetooth.HSBLEPeripheralManager;
import com.lepow.hiremote.misc.IntentAction;

/**
 * 后台运行的Service，随应用启动；连接蓝牙设备后，服务激活
 * 实现定位、录音等功能
 * @author peng
 *
 */
public class BackgroundService extends Service
{
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int characteristicValue = HSBLEPeripheralManager.getInstance().getValueOfCharacteristic(intent);
			switch (characteristicValue)
			{
				case HSBLEPeripheralManager.FIND_PHONE_CHARACTERISTIC_VALUE:

					switch (((HSApplication)HSApplication.getInstance()).getMode())
					{
						case HSApplication.MODE_LOCATE:

							break;
						case HSApplication.MODE_CAPTURE:
							sendBroadcast(new Intent(IntentAction.ACTION_CAPTURE));
							break;
						case HSApplication.MODE_RECORD:

							break;

						default:
							break;
					}

					break;

				case HSBLEPeripheralManager.FIND_PHONE_CHARACTERISTIC_VALUE_LONG:
					break;

				default:
					break;
			}
		}
	};

	@Override
	public void onCreate()
	{
		super.onCreate();
		registerReceiver(broadcastReceiver, new IntentFilter(IntentAction.ACTION_FIND_PHONE));
	}

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

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
