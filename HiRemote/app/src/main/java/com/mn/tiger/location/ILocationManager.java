package com.mn.tiger.location;

/**
 * Created by Dalang on 2015/7/30.
 */
public interface ILocationManager
{
    void requestLocationUpdates();

    void setLocationListener(ILocationListener listener);

    boolean isLocationInChina(TGLocation location);

    public static interface ILocationListener
    {
        void onReceiveLocation(TGLocation location);
    }

    public static enum Provider
    {
        BaiDu,
        Google,
        AMap
    }
}
