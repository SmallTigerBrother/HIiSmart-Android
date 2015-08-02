package com.lepow.hiremote.lbs.data;

import java.io.Serializable;


/**
 * 位置信息
 */
public class LocationInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 定位数据
	 */
	public static final int DATA_TYPE_PINNED_LOCATION = 1;

	/**
	 * 设备断开连接数据
	 */
	public static final int DATA_TYPE_DISCONNECT_LOCATION = 2;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 蓝牙模块UUID
	 */
	private String bleUUID;

	/**
	 * 定位时间
	 */
	private long time;

	/**
	 * 时区
	 */
	private String timeZone;

	/**
	 * 同步状态
	 */
	private int syncStatus;

	/**
	 * 经度
	 */
	private double latitude;

	/**
	 * 纬度
	 */
	private double longitude;

	/**
	 * 地址信息
	 */
	private String address;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 数据类型
	 */
	private int dataType = DATA_TYPE_PINNED_LOCATION;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

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

	public int getDataType()
	{
		return dataType;
	}

	public void setDataType(int dataType)
	{
		this.dataType = dataType;
	}

	public int getSyncStatus()
	{
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus)
	{
		this.syncStatus = syncStatus;
	}

	public void setBleUUID(String bleUUID)
	{
		this.bleUUID = bleUUID;
	}

	public String getBleUUID()
	{
		return bleUUID;
	}

	public String getTimeZone()
	{
		return timeZone;
	}

	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}
}
