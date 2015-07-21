package com.lepow.hiremote.home.present;

import java.util.List;


import com.mn.tiger.app.IView;
import com.lepow.hiremote.connect.data.DeviceInfo;

public interface IHomeView extends IView
{
	void initDeviceBanner(List<DeviceInfo> deviceInfos);

	void turnOnNotificationSetting();

	void turnOffNotificationSetting();

	void turnOnVoiceSetting();

	void turnOffVoiceSetting();
}
