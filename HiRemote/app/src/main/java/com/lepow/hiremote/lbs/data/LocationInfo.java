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
	public static final int DATA_TYPE_PINNED_LOCATION = 0;

	/**
	 * 设备断开连接数据
	 */
	public static final int DATA_TYPE_DISCONNECT_LOCATION = 1;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 蓝牙模块UUID
	 */
	private String peripheralUUID;

	/**
	 * 定位时间
	 */
	private long timestamp;

	/**
	 * 时区
	 */
	private String timeZone;

	/**
	 * 同步状态
	 */
	private int sync;

	/**
	 * 经度
	 */
	private String latitude;

	/**
	 * 纬度
	 */
	private String longitude;

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

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public String getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	
	public String getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(String longitude)
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

	public int getSync()
	{
		return sync;
	}

	public void setSync(int sync)
	{
		this.sync = sync;
	}

	public void setPeripheralUUID(String peripheralUUID)
	{
		this.peripheralUUID = peripheralUUID;
	}

	public String getPeripheralUUID()
	{
		return peripheralUUID;
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
