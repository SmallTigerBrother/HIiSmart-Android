package com.mn.tiger.location;

import com.mn.tiger.log.Logger;

/**
 * Created by Dalang on 2015/7/26.
 * 地址管理类
 */
public class TGLocationManager implements ILocationManager
{
    private static final Logger LOG = Logger.getLogger(TGLocationManager.class);

    /**
     * 单例对象
     */
    private static TGLocationManager instance;

    private ILocationManager curLocationManager;

    private ILocationListener listener;

    private static Provider currentProvider = Provider.AMap;

    public static void init(Provider provider)
    {
        currentProvider = provider;
    }

    public synchronized static TGLocationManager getInstance()
    {
        if(null == instance)
        {
            instance = new TGLocationManager();
            switch (currentProvider)
            {
                case BaiDu:
                    instance.curLocationManager = new BaiduLocationManager();
                    break;
                case AMap:
                    instance.curLocationManager = new AMapLocationManager();
                    break;
                case Google:
                    instance.curLocationManager = new GoogleLocationManager();
                    break;
                default:
                    break;
            }

        }

        return instance;
    }

    public void initAppropriateLocationManager()
    {
        //请求一次定位，判断是不是在中国
        curLocationManager.setLocationListener(new ILocationListener()
        {
            @Override
            public void onReceiveLocation(TGLocation location)
            {
                if (!curLocationManager.isLocationInChina(location))
                {
                    if (!(curLocationManager instanceof GoogleLocationManager))
                    {
                        curLocationManager = new GoogleLocationManager();
                    }
                    currentProvider = Provider.Google;
                }
                curLocationManager.setLocationListener(listener);
            }
        });

        requestLocationUpdates();
    }

    @Override
    public void requestLocationUpdates()
    {
        if(null != curLocationManager)
        {
            LOG.d("[Method:requestLocationUpdates] " + currentProvider);
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

    @Override
    public boolean isLocationInChina(TGLocation location)
    {
        return false;
    }
}
