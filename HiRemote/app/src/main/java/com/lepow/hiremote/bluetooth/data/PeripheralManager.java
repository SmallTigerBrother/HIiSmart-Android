package com.lepow.hiremote.bluetooth.data;

import java.util.ArrayList;
import java.util.List;

public class PeripheralManager
{
	private static PeripheralManager instanse;
	
	public static synchronized PeripheralManager getInstanse()
	{
		if(null == instanse)
		{
			instanse = new PeripheralManager();
		}
		
		return instanse;
	}
	
	private PeripheralManager()
	{
		
	}
	
	public void connectToDevice(DeviceConnectListener listener)
	{
		
	}
	
	public PeripheralInfo getConnectedPeripheral()
	{
		return null;
	}
	
	public List<PeripheralInfo> getAllPeripherals()
	{
		List<PeripheralInfo> peripheralInfos = new ArrayList<PeripheralInfo>();
		PeripheralInfo peripheralInfo = new PeripheralInfo();
		peripheralInfo.setPeripheralName("My Bag");
		peripheralInfo.setSyncTime(23424234);
		
		peripheralInfos.add(peripheralInfo);
		return peripheralInfos;
	}
	
	public static interface DeviceConnectListener
	{
		void onConnected(PeripheralInfo deviceInfo);
		
		void onDisConnected(PeripheralInfo deviceInfo);
	}
	
}
