package com.lepow.hiremote.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.lepow.hiremote.bluetooth.HSBLEPeripheralManager;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.IntentAction;
import com.lepow.hiremote.record.data.RecordDataManager;
import com.lepow.hiremote.record.data.RecordInfo;
import com.lepow.hiremote.setting.AppSettings;
import com.mn.tiger.app.TGApplicationProxy;
import com.mn.tiger.location.ILocationManager;
import com.mn.tiger.location.TGLocation;
import com.mn.tiger.location.TGLocationManager;
import com.mn.tiger.log.Logger;
import com.mn.tiger.media.TGRecorder;

/**
 * 后台运行的Service，随应用启动；连接蓝牙设备后，服务激活
 * 实现定位、录音等功能
 * @author peng
 *
 */
public class BackgroundService extends Service
{
	private static final Logger LOG = Logger.getLogger(BackgroundService.class);

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int characteristicValue = HSBLEPeripheralManager.getInstance().getValueOfFindPhoneCharacteristic(intent);
			switch (characteristicValue)
			{
				case HSBLEPeripheralManager.FIND_PHONE_CHARACTERISTIC_VALUE:

					switch (AppSettings.getMode())
					{
						case AppSettings.MODE_LOCATE:
							//定位
							requestLocation();
							break;
						case AppSettings.MODE_CAPTURE:
							//拍照
							sendBroadcast(new Intent(IntentAction.ACTION_CAPTURE));
							break;
						case AppSettings.MODE_RECORD:
							//录音
							toggleRecord();
							break;

						default:
							break;
					}

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

	/**
	 * 请求定位
	 */
	private static void requestLocation()
	{
		final TGLocationManager locationManager = TGLocationManager.getInstance();
		locationManager.setLocationListener(new ILocationManager.ILocationListener()
		{
			@Override
			public void onReceiveLocation(TGLocation location)
			{
				LocationInfo locationInfo = LocationInfo.fromLocation(location);
				LocationDataManager.getInstance().savePinnedLocation(TGApplicationProxy.getInstance().getApplication(), locationInfo);
				LOG.d("[Method:onReceiveLocation] " + locationInfo.getAddress());

				Intent intent = new Intent(IntentAction.ACTION_PINNED_LOCATION);
				TGApplicationProxy.getInstance().getApplication().sendBroadcast(intent);
				locationManager.removeLocationUpdates();
			}
		});

		locationManager.requestLocationUpdates();
	}

	/**
	 * 开始/停止录音
	 */
	private void toggleRecord()
	{
		final TGRecorder recorder = TGRecorder.getInstance();
		if(recorder.isRecording())
		{
			recorder.stop();
		}
		else
		{
			final long time = System.currentTimeMillis();
			final String fileName = time + ".mp3";

			recorder.start(RecordDataManager.getRecordFilePath(fileName), new TGRecorder.OnRecordListener()
			{
				@Override
				public void onRecordStart(String outputFilePath)
				{
				}

				@Override
				public void onRecording(String outputFilePath, int duration)
				{
				}

				@Override
				public void onRecordStop(String outputFilePath, int duration)
				{
					RecordInfo recordInfo = new RecordInfo();
					recordInfo.setFileName(fileName);
					recordInfo.setTimestamp(time);
					recordInfo.setPeripheralUUID("");
					recordInfo.setTitle("New Record");
					recordInfo.setDuration(duration);
					RecordDataManager.getInstance().saveRecord(BackgroundService.this, recordInfo);

					//通知录音列表界面更新
					TGApplicationProxy.getInstance().getBus().post(recordInfo);
				}
			});
		}
	}
}
