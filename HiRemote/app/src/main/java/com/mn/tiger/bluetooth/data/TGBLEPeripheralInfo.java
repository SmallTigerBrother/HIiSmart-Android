package com.mn.tiger.bluetooth.data;

import java.io.Serializable;
import java.util.UUID;

public class TGBLEPeripheralInfo implements Serializable
{
	private int energy;
	
	private String peripheralName;

	private UUID UUID;
	
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

}
