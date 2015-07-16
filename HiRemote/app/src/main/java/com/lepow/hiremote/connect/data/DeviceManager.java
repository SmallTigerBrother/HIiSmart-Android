package com.lepow.hiremote.connect.data;

import java.util.ArrayList;
import java.util.List;

public class DeviceManager
{
	private static DeviceManager instanse;
	
	public static synchronized DeviceManager getInstanse()
	{
		if(null == instanse)
		{
			instanse = new DeviceManager();
		}
		
		return instanse;
	}
	
	private DeviceManager()
	{
		
	}
	
	public void connectToDevice(DeviceConnectListener listener)
	{
		
	}
	
	public DeviceInfo getConnectedDevice()
	{
		return null;
	}
	
	public List<DeviceInfo> getAllDevices()
	{
		List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDeviceName("My Bag");
		deviceInfo.setSyncTime(23424234);
		
		deviceInfos.add(deviceInfo);
		return deviceInfos;
	}
	
	public static interface DeviceConnectListener
	{
		void onConnected(DeviceInfo deviceInfo);
		
		void onDisConnected(DeviceInfo deviceInfo);
	}
	
}
