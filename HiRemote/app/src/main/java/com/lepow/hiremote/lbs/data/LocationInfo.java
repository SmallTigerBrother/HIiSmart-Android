package com.lepow.hiremote.lbs.data;

import java.io.Serializable;


public class LocationInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long time;
	
	private double latitude;
	
	private double longitude;
	
	private String address;
	
	private String remark;

	private DataType dataType;
	
	public void setTime(long time)
	{
		this.time = time;
	}
	
	public long getTime()
	{
		return time;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getDateString()
	{
		return "";
	}
	
	public String getTimeString()
	{
		return "";
	}
	
	public String getRemark()
	{
		return remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public void setDataType(DataType dataType)
	{
		this.dataType = dataType;
	}

	public DataType getDataType()
	{
		return dataType;
	}

	public static enum DataType
	{
		TYPE_PINNED_LOCATION,
		TYPE_DISCONNECT_LOCATIONA
	}
}
