package com.lepow.hiremote.home.present;

import android.app.Activity;

import com.lepow.hiremote.app.AppSettings;
import com.lepow.hiremote.app.Presenter;
import com.lepow.hiremote.bluetooth.data.PeripheralDataManager;
import com.mn.tiger.bluetooth.event.ConnectPeripheralEvent;
import com.squareup.otto.Subscribe;

public class HomePresenter extends Presenter
{
	private IHomeView view;
	
	public HomePresenter(Activity activity, IHomeView view)
	{
		super(activity);
		this.view = view;
	}

	@Subscribe
	public void onConnectDevice(ConnectPeripheralEvent event)
	{
		switch (event.getState())
		{
			case Connected:
				break;

			case Failed:
				break;

			case BluetoothOff:
				break;

			case Disconnect:
				break;

			default:
				break;
		}
	}


	public void initDevicesAndSettings()
	{
		view.initDeviceBanner(PeripheralDataManager.getInstance().getAllPeripherals());
		
		if(AppSettings.isNotificationSettingOn(getActivity()))
		{
			view.turnOnNotificationSetting();
		}
		else
		{
			view.turnOffNotificationSetting();
		}
		
		if(AppSettings.isVoiceSettingOn(getActivity()))
		{
			view.turnOnVoiceSetting();
		}
		else
		{
			view.turnOffVoiceSetting();
		}
	}
	
	public void turnOnOrOffNotificationSetting()
	{
		if(AppSettings.isNotificationSettingOn(getActivity()))
		{
			AppSettings.setNotificationSetting(getActivity(), false);
			view.turnOffNotificationSetting();
		}
		else
		{
			AppSettings.setNotificationSetting(getActivity(), true);
			view.turnOnNotificationSetting();
		}
	}
	
	public void turnOnOrOffVoiceSetting()
	{
		if(AppSettings.isVoiceSettingOn(getActivity()))
		{
			AppSettings.setVoiceSetting(getActivity(), false);
			view.turnOffVoiceSetting();
		}
		else
		{
			AppSettings.setVoiceSetting(getActivity(), true);
			view.turnOnVoiceSetting();
		}
	}
}
