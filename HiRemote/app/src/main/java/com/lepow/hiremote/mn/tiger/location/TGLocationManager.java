package com.lepow.hiremote.mn.tiger.location;

/**
 * Created by Dalang on 2015/7/26.
 * 地址管理类
 */
public class TGLocationManager implements ILocationManager
{
    private Provider currentProvider;

    /**
     * 单例对象
     */
    private static TGLocationManager instance;

    private BaiduLocationManager baiduLocationManager;

    private ILocationManager curLocationManager;

    private ILocationListener listener;

    private Provider provider = Provider.Unknown;

    public synchronized static TGLocationManager getInstance()
    {
        if(null == instance)
        {
            instance = new TGLocationManager();
            instance.baiduLocationManager = new BaiduLocationManager();
        }

        return instance;
    }

    public void initAppropriateLocationManager()
    {
        baiduLocationManager.checkLocationIsChina(new ILocationListener()
        {
            @Override
            public void onReceiveLocation(TGLocation location)
            {
                if (BaiduLocationManager.isLocationInChina(location))
                {
                    curLocationManager = baiduLocationManager;
                    provider = Provider.Baidu;
                }
                else
                {
                    curLocationManager = new GoogleLocationManager();
                    provider = Provider.Google;
                }
                curLocationManager.setLocationListener(listener);
            }
        });
    }

    @Override
    public void requestLocationUpdates()
    {
        if(null != curLocationManager)
        {
            curLocationManager.requestLocationUpdates();
        }
    }

    @Override
    public void setLocationListener(ILocationListener listener)
    {
        this.listener = listener;
        if(null != curLocationManager)
        {
            curLocationManager.setLocationListener(listener);
        }
    }
}
