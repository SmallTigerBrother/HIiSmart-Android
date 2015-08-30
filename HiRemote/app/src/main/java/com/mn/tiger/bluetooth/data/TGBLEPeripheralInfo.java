package com.mn.tiger.bluetooth.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class TGBLEPeripheralInfo implements Serializable
{
	private int energy;
	
	private String peripheralName;

	private UUID UUID;

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

	public void setUUID(java.util.UUID UUID)
	{
		this.UUID = UUID;
	}

	public java.util.UUID getUUID()
	{
		return UUID;
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
