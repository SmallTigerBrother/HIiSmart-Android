package com.lepow.hiremote.bluetooth.data;

import android.content.Context;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
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

	public static List<PeripheralInfo> getAllPeripherals(Context context)
	{
		try
		{
			List<PeripheralInfo> peripheralInfos = getDBManager(context).findAll(PeripheralInfo.class);
			if(null == peripheralInfos || peripheralInfos.size() == 0)
			{
				return getDefaultPeriperalInfos();
			}
			return peripheralInfos;
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
		try
		{
			getDBManager(context).saveOrUpdate(peripheralInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

}
