package com.lepow.hiremote.record.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.Selector;
import com.mn.tiger.log.Logger;

public class RecordManager
{
	private static final Logger LOG = Logger.getLogger(RecordManager.class);
	
	private static final String DB_NAME = "hismart_records";
	
	private static final int DB_VERSION = 1;
	
	private static RecordManager instanse;
	
	private static TGDBManager getDBManager(Context context)
	{
		return TGDBManager.create(context, DB_NAME, DB_VERSION, null);
	}
	
	public static synchronized RecordManager getInstanse()
	{
		if(null == instanse)
		{
			instanse = new RecordManager();
		}
		
		return instanse;
	}
	
	public List<RecordInfo> findAll(Context context)
	{
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
	
	public List<Object> findAllRecordsSortByTime(Context context)
	{
		List<RecordInfo> recordInfos = findAll(context);
		//TODO 
		return null;
	}
	
	public void saveRecord(Context context, RecordInfo recordInfo)
	{
		try
		{
			getDBManager(context).save(recordInfo);
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
			//删除文件
			
		}
		catch (DbException e)
		{
			LOG.e(e);
		}
	}
}
