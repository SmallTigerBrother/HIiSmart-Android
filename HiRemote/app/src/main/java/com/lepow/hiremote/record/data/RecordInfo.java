package com.lepow.hiremote.record.data;

import java.io.Serializable;

/**
 * 录音数据
 */
public class RecordInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int _id;

	private static final int SYNC_FALSE = 0;

	private static final int SYNC_TRUE = 1;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 设备的UUID
	 */
	private String peripheralUUID;

	/**
	 * 记录名称
	 */
	private String title;

	/**
	 * 录音时间
	 */
	private long timestamp;

	/**
	 * 录音时长
	 */
	private int duration;

	/**
	 * 录音本地文件名称
	 */
	private String fileName;

	/**
	 * 同步状态,
	 */
	private int sync = SYNC_FALSE;

	/**
	 * 录音文件下载地址
	 */
	private String recordFileUrl;

	/**
	 * 录音文件MD5校验码
	 */
	private String md5Code;

	public int get_id()
	{
		return _id;
	}

	public void set_id(int _id)
	{
		this._id = _id;
	}

	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String name)
	{
		this.title = name;
	}
	
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public String getRecordFilePath()
	{
		return RecordDataManager.getRecordFilePath(fileName);
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getPeripheralUUID()
	{
		return peripheralUUID;
	}

	public void setPeripheralUUID(String peripheralUUID)
	{
		this.peripheralUUID = peripheralUUID;
	}

	public int getSync()
	{
		return sync;
	}

	public void setSync(int sync)
	{
		this.sync = sync;
	}

	public String getRecordFileUrl()
	{
		return recordFileUrl;
	}

	public void setRecordFileUrl(String recordFileUrl)
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

}
