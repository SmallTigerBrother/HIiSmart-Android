package com.lepow.hiremote.bluetooth.data;

import java.util.ArrayList;
import java.util.List;

public class PeripheralDataManager
{

	private PeripheralDataManager()
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
	
}
