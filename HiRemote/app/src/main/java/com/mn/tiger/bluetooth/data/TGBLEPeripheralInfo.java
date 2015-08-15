package com.mn.tiger.bluetooth.data;

import com.mn.tiger.bluetooth.TGBluetoothManager;
import com.mn.tiger.datastorage.db.annotation.Transient;

import java.io.Serializable;

public class TGBLEPeripheralInfo implements Serializable
{
	private int energy;
	
	private String peripheralName;
	
	@Transient
	private TGBluetoothManager.ConnectState state;

	private String UUID;

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


	public void setState(TGBluetoothManager.ConnectState state)
	{
		this.state = state;
	}

	public TGBluetoothManager.ConnectState getState()
	{
		return state;
	}

	public String getUUID()
	{
		return UUID;
	}

	public void setUUID(String UUID)
	{
		this.UUID = UUID;
	}
}
