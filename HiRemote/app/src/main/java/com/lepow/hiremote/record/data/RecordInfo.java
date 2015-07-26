package com.lepow.hiremote.record.data;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class RecordInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;

	private long time;
	
	private long duration;
	
	private String filePath;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setTime(long time)
	{
		this.time = time;
	}
	
	public long getTime()
	{
		return time;
	}
	
	public String getDateString()
	{
		return new Date(time).toString();
	}
	
	public long getDuration()
	{
		return duration;
	}
	
	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public String getDurationString()
	{
		return duration + "";
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	public File getRecordFile()
	{
		return null;
	}
}
