package com.lepow.hiremote.lbs.locate;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mn.tiger.app.TGApplication;

/**
 * Created by Dalang on 2015/7/26.
 * 百度定位管理类
 */
public class BaiduLocationManager
{
    private LocationClient locationClient;

    private BDLocationListener locationListener = new BDLocationListener()
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {
            //发通知界面处理
            TGApplication.getBus().post(bdLocation);
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

    /**
     * 请求定位
     */
    public void requestLocationUpdates()
    {
        locationClient.start();
    }
}
