package com.lepow.hiremote.bluetooth.data;

import com.lepow.hiremote.R;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.bluetooth.data.TGBLEPeripheralInfo;
import com.mn.tiger.datastorage.db.annotation.Transient;

import java.io.Serializable;

public class PeripheralInfo implements Serializable
{
	private int _id;

	private int energy;
	
	private String peripheralName = "";
	
	private long syncTime;

	@Transient
	private LocationInfo location;

	@Transient
	private boolean connected = false;

	private String macAddress = "";

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

	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}

	public String getMacAddress()
	{
		return macAddress;
	}
	public boolean isConnected()
	{
		return connected;
	}

	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}

	public static PeripheralInfo fromBLEPeripheralInfo(TGBLEPeripheralInfo blePeripheralInfo)
	{
		if(null != blePeripheralInfo)
		{
			PeripheralInfo peripheralInfo = PeripheralDataManager.findPeripheral(TGApplication.getInstance(), blePeripheralInfo.getMacAddress());
			if(peripheralInfo.equals(NULL_OBJECT))
			{
				peripheralInfo = new PeripheralInfo();
				peripheralInfo.setPeripheralName(blePeripheralInfo.getPeripheralName());
				peripheralInfo.setMacAddress(blePeripheralInfo.getMacAddress());
			}

			peripheralInfo.setEnergy(blePeripheralInfo.getEnergy());
			peripheralInfo.setConnected(true);
			return peripheralInfo;
		}
		else
		{
			return  NULL_OBJECT;
		}
	}

	public static final PeripheralInfo NULL_OBJECT = new PeripheralInfo()
	{
		@Override
		public int getEnergy()
		{
			return 0;
		}

		@Override
		public String getPeripheralName()
		{
			return TGApplication.getInstance().getString(R.string.no_device);
		}
	};
}
