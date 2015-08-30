package com.lepow.hiremote.record.data;

import android.content.Context;
import android.os.Environment;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.Selector;
import com.mn.tiger.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordDataManager
{
	private static final Logger LOG = Logger.getLogger(RecordDataManager.class);
	
	private static final String DB_NAME = "hismart_records";
	
	private static final int DB_VERSION = 1;
	
	private static RecordDataManager instance;
	
	private static TGDBManager getDBManager(Context context)
	{
		return TGDBManager.create(context, DB_NAME, DB_VERSION, null);
	}
	
	public static RecordDataManager getInstance()
	{
		if(null == instance)
		{
			synchronized (RecordDataManager.class)
			{
				if(null == instance)
				{
					instance = new RecordDataManager();
				}
			}
		}
		
		return instance;
	}
	
	public List<RecordInfo> findAllRecordsSortByTime(Context context)
	{
//		RecordInfo recordInfo = new RecordInfo();
//		recordInfo.setDuration(6000);
//		recordInfo.setFileName("test.aac");
//		recordInfo.setTimestamp(System.currentTimeMillis());
//		recordInfo.setTitle("New Record");
//
//		ArrayList<RecordInfo> recordInfos = new ArrayList<RecordInfo>();
//		recordInfos.add(recordInfo);
//
//		return recordInfos;
		try
		{
			return getDBManager(context).findAll(Selector.from(RecordInfo.class).orderBy("time"));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<RecordInfo>();
		}
	}

	public List<RecordInfo> findAllRecordsSortByTime(Context context, String queryText)
	{
		try
		{
			return getDBManager(context).findAll(Selector.from(RecordInfo.class).orderBy("time")
					.and("name", "like", queryText));
		}
		catch (DbException e)
		{
			LOG.e(e);
			return new ArrayList<RecordInfo>();
		}
	}
	
	public void saveRecord(Context context, RecordInfo recordInfo)
	{
		try
		{
			getDBManager(context).saveOrUpdate(recordInfo);
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}	
	
	public void removeRecord(Context context, RecordInfo recordInfo)
	{
		try
		{
			//删除数据库记录
			getDBManager(context).delete(recordInfo);
			//TODO 删除文件
			
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}

	public static String getRecordFilePath(String fileName)
	{
		return TGApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + fileName;
	}
}
