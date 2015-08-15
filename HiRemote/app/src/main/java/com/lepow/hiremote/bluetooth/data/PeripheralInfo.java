package com.lepow.hiremote.bluetooth.data;

import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.bluetooth.TGBluetoothManager;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;
import com.mn.tiger.datastorage.db.annotation.Transient;

import java.io.Serializable;

public class PeripheralInfo implements Serializable
{
	private int energy;
	
	private String peripheralImage;
	
	private String peripheralName;
	
	private long syncTime;

	@Transient
	private TGBluetoothManager.ConnectState state = TGBluetoothManager.ConnectState.Disconnect;

	@Transient
	private LocationInfo location;

	public int getEnergy()
	{
		return energy;
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public String getPeripheralImage()
	{
		return peripheralImage;
	}

	public void setPeripheralImage(String peripheralImage)
	{
		this.peripheralImage = peripheralImage;
	}

	public String getPeripheralName()
	{
		return peripheralName;
	}

	public void setPeripheralName(String peripheralName)
	{
		this.peripheralName = peripheralName;
	}

	public long getSyncTime()
	{
		return syncTime;
	}

	public void setSyncTime(long syncTime)
	{
		this.syncTime = syncTime;
	}

	public void setState(TGBluetoothManager.ConnectState state)
	{
		this.state = state;
	}

	public TGBluetoothManager.ConnectState getState()
	{
		return state;
	}

	public void setLocation(LocationInfo location)
	{
		this.location = location;
	}

	public LocationInfo getLocation()
	{
		return location;
	}

	public static PeripheralInfo fromBLEPeripheralInfo(TGBLEPeripheralInfo blePeripheralInfo)
	{
		PeripheralInfo peripheralInfo = new PeripheralInfo();
		peripheralInfo.setPeripheralName(blePeripheralInfo.getPeripheralName());
		peripheralInfo.setEnergy(blePeripheralInfo.getEnergy());
		peripheralInfo.setState(blePeripheralInfo.getState());

		return peripheralInfo;
	}
}
