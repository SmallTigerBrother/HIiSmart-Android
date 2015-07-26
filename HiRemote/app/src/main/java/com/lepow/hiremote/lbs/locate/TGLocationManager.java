package com.lepow.hiremote.lbs.locate;

/**
 * Created by Dalang on 2015/7/26.
 * 地址管理类
 */
public class TGLocationManager
{
    private Provider currentProvider;

    /**
     * 单例对象
     */
    private static TGLocationManager instance;

    public synchronized static TGLocationManager getInstance()
    {
        if(null == instance)
        {
            instance = new TGLocationManager();
        }

        return instance;
    }

    public static enum Provider
    {
        Baidu,
        Google
    }
}
