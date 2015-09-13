package com.lepow.hiremote.bluetooth.data;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.WhereBuilder;
import com.mn.tiger.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class PeripheralDataManager
{
	private static final Logger LOG = Logger.getLogger(PeripheralDataManager.class);

	private static final String DB_NAME = "peripheral_data";

	private static final int DB_VERSION = 1;

	private PeripheralDataManager()
	{

	}

	private static TGDBManager getDBManager(Context context)
	{
		return TGDBManager.create(context, DB_NAME, DB_VERSION, null);
	}

	public static List<PeripheralInfo> getAllPeripherals(Context context, PeripheralInfo connectedPeripheral,
														 PeripheralInfo disConnectedPeripheral)
	{
		try
		{
			List<PeripheralInfo> peripheralInfos = getDBManager(context).findAll(PeripheralInfo.class);
			if(null == peripheralInfos || peripheralInfos.size() == 0)
			{
				return getDefaultPeriperalInfos();
			}
			else
			{
				if(null != connectedPeripheral && !connectedPeripheral.equals(PeripheralInfo.NULL_OBJECT))
				{
					for (int i = 0; i < peripheralInfos.size(); i++)
					{
						PeripheralInfo peripheralInfo = peripheralInfos.get(i);
						if(peripheralInfo.getMacAddress().equals(connectedPeripheral.getMacAddress()))
						{
							connectedPeripheral.setEnergy(peripheralInfo.getEnergy());
							connectedPeripheral.setPeripheralName(peripheralInfo.getPeripheralName());
							peripheralInfos.set(i, connectedPeripheral);
						}
					}
				}

				if(null != disConnectedPeripheral && !disConnectedPeripheral.equals(PeripheralInfo.NULL_OBJECT))
				{
					for (int i = 0; i < peripheralInfos.size(); i++)
					{
						PeripheralInfo peripheralInfo = peripheralInfos.get(i);
						if(peripheralInfo.getMacAddress().equals(disConnectedPeripheral.getMacAddress()))
						{
							disConnectedPeripheral.setConnected(false);
							peripheralInfos.set(i, disConnectedPeripheral);
						}
					}
				}

				return peripheralInfos;
			}
		}
		catch (DbException e)
		{
			LOG.e(e);
			return  getDefaultPeriperalInfos();
		}
	}

	private static List<PeripheralInfo> getDefaultPeriperalInfos()
	{
		List<PeripheralInfo> peripheralInfos = new ArrayList<PeripheralInfo>();
		peripheralInfos.add(PeripheralInfo.NULL_OBJECT);
		return peripheralInfos;
	}

	public static void savePeripheral(Context context, PeripheralInfo peripheralInfo)
	{
		if(null != peripheralInfo && !peripheralInfo.equals(PeripheralInfo.NULL_OBJECT))
		{
			try
			{
				PeripheralInfo savedPeripheral = getDBManager(context).findFirst(peripheralInfo.getClass(), WhereBuilder.b("macAddress", "=", peripheralInfo.getMacAddress()));
				if(null == savedPeripheral)
				{
					getDBManager(context).save(peripheralInfo);
				}
				else
				{
					if(peripheralInfo.getEnergy() == 0)
					{
						peripheralInfo.setEnergy(savedPeripheral.getEnergy());
					}
					getDBManager(context).update(peripheralInfo, WhereBuilder.b("macAddress", "=", peripheralInfo.getMacAddress()));
				}
			}
			catch (DbException e)
			{
				LOG.e(e);
			}
		}
	}

	public static PeripheralInfo findPeripheral(Context context, String macAddress)
	{
		if(!TextUtils.isEmpty(macAddress))
		{
			try
			{
				PeripheralInfo savedPeripheral = getDBManager(context).findFirst(PeripheralInfo.class, WhereBuilder.b("macAddress", "=", macAddress));
				return savedPeripheral != null ? savedPeripheral : PeripheralInfo.NULL_OBJECT;
			}
			catch (DbException e)
			{
				LOG.e(e);
			}
		}
		return PeripheralInfo.NULL_OBJECT;
	}

}
