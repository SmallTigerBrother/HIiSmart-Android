package com.lepow.hiremote.connect.data;

public class DeviceInfo
{
	private int energy;
	
	private String deviceImage;
	
	private String deviceName;
	
	private long syncTime;

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
	
}
