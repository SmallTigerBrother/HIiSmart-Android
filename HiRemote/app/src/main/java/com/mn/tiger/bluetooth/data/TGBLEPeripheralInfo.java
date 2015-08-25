package com.mn.tiger.bluetooth.data;

import com.mn.tiger.bluetooth.TGBluetoothManager;
import com.mn.tiger.datastorage.db.annotation.Transient;

import java.io.Serializable;
import java.util.UUID;

public class TGBLEPeripheralInfo implements Serializable
{
	private int energy;
	
	private String peripheralName;

	private UUID UUID;
	
	@Transient
	private TGBluetoothManager.ConnectState state;

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

	public void setState(TGBluetoothManager.ConnectState state)
	{
		this.state = state;
	}

	public TGBluetoothManager.ConnectState getState()
	{
		return state;
	}

}
