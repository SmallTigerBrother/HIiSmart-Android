package com.mn.tiger.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mn.tiger.app.TGApplication;

/**
 * Created by Dalang on 2015/7/26.
 * 百度定位管理类
 */
public class BaiduLocationManager implements ILocationManager
{
    private LocationClient locationClient;

    private ILocationListener listener;

    private BDLocationListener locationListener = new BDLocationListener()
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {
            //发通知界面处理
            if(null != listener)
            {
                listener.onReceiveLocation(TGLocation.initWith(bdLocation));
            }
        }
    };

    /**
     * 初始化
     */
    BaiduLocationManager()
    {
        locationClient = new LocationClient(TGApplication.getInstance());
        locationClient.registerLocationListener(locationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIgnoreKillProcess(true);

        locationClient.setLocOption(option);
    }

    public void checkLocationIsChina(final ILocationListener listener)
    {
        locationClient.registerLocationListener(new BDLocationListener()
        {
            @Override
            public void onReceiveLocation(BDLocation bdLocation)
            {
                locationClient.unRegisterLocationListener(this);
                TGLocation location = TGLocation.initWith(bdLocation);
                location.setTime(System.currentTimeMillis());
                listener.onReceiveLocation(location);
            }
        });
    }

    /**
     * 请求定位
     */
    @Override
    public void requestLocationUpdates()
    {
        locationClient.start();
    }

    @Override
    public void setLocationListener(ILocationListener listener)
    {
        this.listener = listener;
    }

    public boolean isLocationInChina(TGLocation location)
    {
        return true;
    }
}
