package com.lepow.hiremote.connect.data;

import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.bluetooth.TGBluetoothManager;
import com.mn.tiger.datastorage.db.annotation.Transient;

import java.io.Serializable;

public class DeviceInfo implements Serializable
{
	private int energy;
	
	private String deviceImage;
	
	private String deviceName;
	
	private long syncTime;

	@Transient
	private TGBluetoothManager.ConnectState state;

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

	public String getDeviceImage()
	{
		return deviceImage;
	}

	public void setDeviceImage(String deviceImage)
	{
		this.deviceImage = deviceImage;
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
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
}
