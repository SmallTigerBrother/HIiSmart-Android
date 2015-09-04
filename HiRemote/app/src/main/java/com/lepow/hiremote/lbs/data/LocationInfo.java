package com.lepow.hiremote.lbs.data;

import com.mn.tiger.location.TGLocation;

import java.io.Serializable;
import java.util.Calendar;


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

	private int _id;

	/**
	 * 用户Id
	 */
	private String userId = "";

	/**
	 * 蓝牙模块UUID
	 */
	private String peripheralUUID = "";

	/**
	 * 定位时间
	 */
	private long timestamp;

	/**
	 * 同步状态
	 */
	private int sync;

	/**
	 * 经度
	 */
	private String latitude ="";

	/**
	 * 纬度
	 */
	private String longitude = "";

	/**
	 * 地址信息
	 */
	private String address = "";

	/**
	 * 备注
	 */
	private String remark = "";

	/**
	 * 数据类型
	 */
	private int dataType = DATA_TYPE_PINNED_LOCATION;

	public int get_id()
	{
		return _id;
	}

	public void set_id(int _id)
	{
		this._id = _id;
	}

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
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR);
	}
	
	public String getTimeString()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
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

	public static LocationInfo fromLocation(TGLocation location)
	{
		LocationInfo locationInfo = new LocationInfo();
		locationInfo.setLatitude(location.getLatitude() + "");
		locationInfo.setLongitude(location.getLongitude() + "");
		locationInfo.setTimestamp(location.getTime());
		locationInfo.setAddress(location.getCountry() + location.getProvince() + location.getCity() +
				location.getStreet() + location.getAddress());
		return locationInfo;
	}
}
