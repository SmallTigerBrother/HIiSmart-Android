package com.lepow.hiremote.lbs.data;

import android.content.Context;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.Selector;
import com.mn.tiger.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class LocationDataManager
{
	private volatile static LocationDataManager instance;

	private static final String LOCATION_DB_NAME = "location_db";

	private static final int DB_VERSION = 1;

	private static final Logger LOG = Logger.getLogger(LocationDataManager.class);

	public static LocationDataManager getInstance()
	{
		if (null == instance)
		{
			synchronized (LocationDataManager.class)
			{
				if(null == instance)
				{
					instance = new LocationDataManager();
				}
			}
		}

		return instance;
	}

	private LocationDataManager()
	{

	}

	private TGDBManager getLocationDBManager(Context context)
	{
		return TGDBManager.create(context, LOCATION_DB_NAME, DB_VERSION, null);
	}

	public void savePinnedLocation(Context context, LocationInfo locationInfo)
	{
		try
		{
			locationInfo.setDataType(LocationInfo.DATA_TYPE_PINNED_LOCATION);
			getLocationDBManager(context).saveOrUpdate(locationInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

	public void removePinnedLocation(Context context, LocationInfo locationInfo)
	{
		try
		{
			locationInfo.setDataType(LocationInfo.DATA_TYPE_PINNED_LOCATION);
			getLocationDBManager(context).delete(locationInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

	public List<LocationInfo> findAllPinnedLocationSortByTime(Context context)
	{
//		ArrayList<LocationInfo> locationInfos = new ArrayList<LocationInfo>();
//
//		for (int i = 0; i < 20; i++)
//		{
//			LocationInfo locationInfo = new LocationInfo();
//			locationInfo.setAddress("广东省深圳市南山区龙珠大道72号");
//			locationInfo.setTimestamp(System.currentTimeMillis());
//			locationInfos.add(locationInfo);
//		}
//
//		return locationInfos;

		try
		{
			return getLocationDBManager(context).findAll(Selector
					.from(LocationInfo.class)
					.where("dataType", "=", LocationInfo.DATA_TYPE_PINNED_LOCATION)
					.orderBy("timestamp", true));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

	public List<LocationInfo> findAllPinnedLocationSortByTime(Context context, String queryText)
	{
		try
		{
			return getLocationDBManager(context).findAll(Selector
					.from(LocationInfo.class)
					.where("dataType", "=", LocationInfo.DATA_TYPE_PINNED_LOCATION)
					.orderBy("timestamp", true)
					.and("address", "like", queryText)
					.or("remark", "like", queryText));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

	public void saveDisconnectedLocation(Context context, LocationInfo locationInfo)
	{
		try
		{
			locationInfo.setDataType(LocationInfo.DATA_TYPE_DISCONNECT_LOCATION);
			getLocationDBManager(context).saveOrUpdate(locationInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

	public void removeDisconnectedLocation(Context context, LocationInfo locationInfo)
	{
		try
		{
			locationInfo.setDataType(LocationInfo.DATA_TYPE_DISCONNECT_LOCATION);
			getLocationDBManager(context).delete(locationInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

	public List<LocationInfo> findAllDisconnectedLocationOderByTime(Context context)
	{
		try
		{
			return getLocationDBManager(context).findAll(Selector
					.from(LocationInfo.class)
					.where("dataType", "=", LocationInfo.DATA_TYPE_DISCONNECT_LOCATION)
					.orderBy("timestamp", true));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

	public LocationInfo findLatestDisconnectedLocation(Context context)
	{
		try
		{
			return getLocationDBManager(context).findFirst(Selector
					.from(LocationInfo.class)
					.where("dataType", "=", LocationInfo.DATA_TYPE_DISCONNECT_LOCATION)
					.orderBy("timestamp", true));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return null;
		}
	}

	public List<LocationInfo> findAllDisconnectedLocationOderByTime(Context context,String queryText)
	{
		try
		{
			return getLocationDBManager(context).findAll(Selector
					.from(LocationInfo.class)
					.where("dataType", "=", LocationInfo.DATA_TYPE_DISCONNECT_LOCATION)
					.orderBy("timestamp", true)
					.and("address", "like", queryText)
					.or("remark", "like", queryText));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

}
