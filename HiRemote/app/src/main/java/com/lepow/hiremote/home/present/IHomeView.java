package com.lepow.hiremote.home.present;

import java.util.List;


import com.mn.tiger.app.IView;
import com.lepow.hiremote.bluetooth.data.PeripheralInfo;

public interface IHomeView extends IView
{
	void initDeviceBanner(List<PeripheralInfo> deviceInfos);

	void turnOnNotificationSetting();

	void turnOffNotificationSetting();

	void turnOnVoiceSetting();

	void turnOffVoiceSetting();
}
