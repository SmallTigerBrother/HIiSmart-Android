package com.lepow.hiremote.bluetooth.data;

import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;
import com.mn.tiger.datastorage.db.annotation.Transient;

import java.io.Serializable;

public class PeripheralInfo implements Serializable
{
	private int _id;

	private String UUID = "";

	private int energy;
	
	private String peripheralImage;
	
	private String peripheralName;
	
	private long syncTime;

	@Transient
	private LocationInfo location;

	public int get_id()
	{
		return _id;
	}

	public void set_id(int _id)
	{
		this._id = _id;
	}

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

	public void setLocation(LocationInfo location)
	{
		this.location = location;
	}

	public LocationInfo getLocation()
	{
		return location;
	}

	public String getUUID()
	{
		return UUID;
	}

	public void setUUID(String UUID)
	{
		this.UUID = UUID;
	}

	public static PeripheralInfo fromBLEPeripheralInfo(TGBLEPeripheralInfo blePeripheralInfo)
	{
		PeripheralInfo peripheralInfo = new PeripheralInfo();
		peripheralInfo.setPeripheralName(blePeripheralInfo.getPeripheralName());
		peripheralInfo.setEnergy(blePeripheralInfo.getEnergy());

		return peripheralInfo;
	}

	public static PeripheralInfo NULL_OBJECT = new PeripheralInfo()
	{
		@Override
		public String getUUID()
		{
			return super.getUUID();
		}

		@Override
		public int getEnergy()
		{
			return 0;
		}

		@Override
		public String getPeripheralName()
		{
			return "无设备";
		}

		@Override
		public String getPeripheralImage()
		{
			return "add_device";
		}
	};
}
