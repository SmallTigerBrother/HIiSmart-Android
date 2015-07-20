package com.lepow.hiremote.lbs.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.Selector;
import com.mn.tiger.log.Logger;

public class LocationManager
{
	private static LocationManager instanse;

	private static final String PINNED_LOCATION_DB_NAME = "pinned_location_db";

	private static final String DISCONNECTED_LOCATION_DB_NAME = "disconnected_location_db";

	private static final Logger LOG = Logger.getLogger(LocationManager.class);

	public static synchronized LocationManager getInstanse()
	{
		if (null == instanse)
		{
			instanse = new LocationManager();
		}

		return instanse;
	}

	private LocationManager()
	{

	}

	private TGDBManager getPinnedLocationDBManager(Context context)
	{
		return TGDBManager.create(context, PINNED_LOCATION_DB_NAME, 1, null);
	}

	private TGDBManager getDisconnectedLocationDBManager(Context context)
	{
		return TGDBManager.create(context, DISCONNECTED_LOCATION_DB_NAME, 1, null);
	}

	public void savePinnedLocation(Context context, LocationInfo locationInfo)
	{
		try
		{
			getPinnedLocationDBManager(context).saveOrUpdate(locationInfo);
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
			getPinnedLocationDBManager(context).delete(locationInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

	public List<LocationInfo> findAllPinnedLocationSortByTime(Context context)
	{
		try
		{
			return getPinnedLocationDBManager(context).findAll(
					Selector.from(LocationInfo.class).orderBy("time"));
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
			return getPinnedLocationDBManager(context).findAll(
					Selector.from(LocationInfo.class).orderBy("time")
							.and("address", "like", queryText).or("remark", "like", queryText));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

	private List<Object> insertTimeSectionInfo(List<LocationInfo> locationInfos)
	{
		ArrayList<Object> results = new ArrayList<Object>();

		// TODO 加入时间信息
		return results;
	}

	public void saveDisconnectedLocation(Context context, LocationInfo locationInfo)
	{
		try
		{
			getDisconnectedLocationDBManager(context).saveOrUpdate(locationInfo);
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
			getDisconnectedLocationDBManager(context).delete(locationInfo);
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
			return getDisconnectedLocationDBManager(context).findAll(
					Selector.from(LocationInfo.class).orderBy("time"));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

	public List<LocationInfo> findAllDisconnectedLocationOderByTime(Context context,String queryText)
	{
		try
		{
			return getDisconnectedLocationDBManager(context).findAll(
					Selector.from(LocationInfo.class).orderBy("time")
							.and("address", "like", queryText).or("remark", "like", queryText));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<LocationInfo>();
		}
	}

}
