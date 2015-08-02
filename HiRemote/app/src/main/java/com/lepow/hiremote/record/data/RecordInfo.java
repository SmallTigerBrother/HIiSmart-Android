package com.lepow.hiremote.record.data;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * 录音数据
 */
public class RecordInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 设备的UUID
	 */
	private String bleUUID;

	/**
	 * 记录名称
	 */
	private String name;

	/**
	 * 录音时间
	 */
	private long time;

	/**
	 * 时区
	 */
	private String timeZone;

	/**
	 * 录音时长
	 */
	private long duration;

	/**
	 * 录音本地文件名称
	 */
	private String fileName;

	/**
	 * 文件格式
	 */
	private String fileFormat;

	/**
	 * 同步状态
	 */
	private int syncStatus;

	/**
	 * 录音文件下载地址
	 */
	private int recordFileUrl;

	/**
	 * 录音文件MD5校验码
	 */
	private String md5Code;
	
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
	
	public File getRecordFile()
	{
		return null;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getBleUUID()
	{
		return bleUUID;
	}

	public void setBleUUID(String bleUUID)
	{
		this.bleUUID = bleUUID;
	}

	public String getTimeZone()
	{
		return timeZone;
	}

	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}

	public int getSyncStatus()
	{
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus)
	{
		this.syncStatus = syncStatus;
	}

	public int getRecordFileUrl()
	{
		return recordFileUrl;
	}

	public void setRecordFileUrl(int recordFileUrl)
	{
		this.recordFileUrl = recordFileUrl;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getMd5Code()
	{
		return md5Code;
	}

	public void setMd5Code(String md5Code)
	{
		this.md5Code = md5Code;
	}

	public String getFileFormat()
	{
		return fileFormat;
	}

	public void setFileFormat(String fileFormat)
	{
		this.fileFormat = fileFormat;
	}
}
