package com.mn.tiger.bluetooth.data;

import java.io.Serializable;
import java.util.HashMap;

public class TGBLEPeripheralInfo implements Serializable
{
	private int energy;
	
	private String peripheralName;

	private String macAddress;

	private HashMap<String, Object> values = new HashMap<String, Object>();
	
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

	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}

	public String getMacAddress()
	{
		return macAddress;
	}

	public void putValue(String key, Object value)
	{
		values.put(key, value);
	}

	public Object getValue(String key, Object defaultValue)
	{
		Object value = values.get(key);
		return null != value ? value : defaultValue;
	}
}
