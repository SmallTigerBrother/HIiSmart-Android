package com.lepow.hiremote.request;

/**
 * 请求结果
 * @param <T>
 */
public class Response<T>
{
	/**
	 * 请求成功
	 */
	public final static int RESULT_SUCCESS = 0;
	
	/**
	 * 请求失败
	 */
	public final static int RESULT_FAILED = 1;
	
	/**
	 * 请求过期
	 */
	public final static int RESULT_EXPIRED = 2;
	
	public Response()
	{
	}
	
	/**
	 * 0代表成功，1代表失败, 2代表已过期，已失效 
	 */
	public transient int code;
	
	/**
	 * 异常信息
	 */
	public transient String message = "";
	
	/**
	 * 结果数据
	 */
    public transient T data;
    
    /**
     * 原始数据
     */
    public transient String rawData;
}
