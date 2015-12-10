package com.mn.tiger.location;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.lepow.hiremote.R;
import com.mn.tiger.app.TGApplicationProxy;
import com.mn.tiger.log.Logger;
import com.mn.tiger.utility.ToastUtils;

/**
 * Created by Dalang on 2015/7/26.
 */
public class GoogleLocationManager implements ILocationManager
{
    private static final Logger LOG = Logger.getLogger(GoogleLocationManager.class);

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * 上一次定位的地址
     */
    private Location lastLocation;

    private TGLocation lastTGLocation;

    /**
     * 系统的位置管理器
     */
    private LocationManager locationManager;

    private ILocationListener listener;

    public GoogleLocationManager()
    {
        locationManager = (LocationManager) TGApplicationProxy.getInstance().getApplication().getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 请求地址更新
     */
    @Override
    public void requestLocationUpdates()
    {
        LOG.i("[Method:requestLocationUpdates]");
        Application application = TGApplicationProxy.getInstance().getApplication();
        if(PackageManager.PERMISSION_GRANTED == application.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION")
                && PackageManager.PERMISSION_GRANTED == application.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION"))
        {
            requestGPSLocationUpdates();
            requestNetworkLocationUpdates();
        }
        else
        {
            ToastUtils.showToast(application, R.string.location_permission_deny);
        }
    }

    @Override
    public void setLocationListener(ILocationListener listener)
    {
        this.listener = listener;
    }

    /**
     * 请求GPS定位更新
     */
    private void requestGPSLocationUpdates()
    {
        LOG.i("[Method:requestGPSLocationUpdates]");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 20, gpsLocationListener);
    }

    /**
     * 请求网络定位地址更新
     */
    private void requestNetworkLocationUpdates()
    {
        LOG.i("[Method:requestNetworkLocationUpdates]");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2 * 1000, 20, networkLocationListener);
    }

    /**
     * 删除网络定位监听器
     */
    private void removeNetworkLocationUpdates()
    {
        locationManager.removeUpdates(networkLocationListener);
    }

    @Override
    public void removeLocationUpdates()
    {
        locationManager.removeUpdates(networkLocationListener);
        locationManager.removeUpdates(gpsLocationListener);
    }

    @Override
    public void destroy()
    {
    }

    /**
     * gps定位监听对象
     */
    private LocationListener gpsLocationListener = new LocationListener()
    {
        /**
         * 是否已删除网络定位监听器的控制参数
         */
        private boolean isRemoveNetworkListener = false;

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            LOG.d("[Method:onStatusChanged] provider == " + provider + "status == " + status);
            //若GPS定位不可用，则启动网络定位
            if (LocationProvider.OUT_OF_SERVICE == status)
            {
                LOG.d("[Method:onStatusChanged] GPS out of service");
                requestNetworkLocationUpdates();
                isRemoveNetworkListener = false;
            }
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onLocationChanged(Location location)
        {
            LOG.d("[Method:onLocationChanged] provider == " + location.getProvider());
            //判断当前地址和上一次定位的结果哪个更加精确，若当前定位的地址更加精确，通知更新地址
            if (isBetterLocation(location, lastLocation))
            {
                updateLocation(location);
            }
            else
            {
                updateLocation(lastLocation);
            }

            //删除网络定位监听接口
            if (location != null && !isRemoveNetworkListener)
            {
                removeNetworkLocationUpdates();
                isRemoveNetworkListener = true;
            }
        }
    };

    /**
     * 网络定位监听类
     */
    private LocationListener networkLocationListener = new LocationListener()
    {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onLocationChanged(Location location)
        {
            LOG.d("[Method:onLocationChanged] provider == " + location.getProvider());
            //判断当前地址和上一次定位的结果哪个更加精确，若当前定位的地址更加精确，通知更新地址
            if (isBetterLocation(location, lastLocation))
            {
                updateLocation(location);
            }
            else
            {
                updateLocation(lastLocation);
            }
        }
    };

    /**
     * 更新地址
     * @param location
     */
    private void updateLocation(final Location location)
    {
        LOG.i("[Method:updateLocation]");
        this.lastLocation = location;
        GoogleGeoCoding.geoCoding(location.getLatitude(), location.getLongitude(), new GoogleGeoCoding.GeoCodeListener()
        {
            @Override
            public void onGeoCodingSuccess(GoogleGeoCodeResult result)
            {
                LOG.d("[Method:onGeoCodingSuccess]");
                TGLocation tgLocation = TGLocation.initWith(location);
                tgLocation.setTime(System.currentTimeMillis());
                if(result.getResults().length > 0)
                {
                    GoogleAddressResult addressResult = result.getResults()[0];
                    tgLocation.setAddress(addressResult.getFormatted_address());
                }
                GoogleLocationManager.this.lastTGLocation = tgLocation;
                //发通知界面处理
                if (null != listener)
                {
                    listener.onReceiveLocation(tgLocation);
                }
            }

            @Override
            public void onGeoCodingError(int code, String message)
            {
                LOG.e("[Method:onGeoCodingError]");
            }
        });

    }

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     *(该方法由Google提供)
     * @param location
     *            The new Location that you want to evaluate
     * @param currentBestLocation
     *            The current Location fix, to which you want to compare the new
     *            one
     */
    private boolean isBetterLocation(Location location, Location currentBestLocation)
    {
        if (currentBestLocation == null)
        {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer)
        {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        }
        else if (isSignificantlyOlder)
        {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate)
        {
            return true;
        }
        else if (isNewer && !isLessAccurate)
        {
            return true;
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
        {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2)
    {
        if (provider1 == null)
        {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * 判断位置是否在中国
     * @param location
     * @return
     */
    @Override
    public boolean isLocationInChina(TGLocation location)
    {
        if(location.getLatitude() != 0 && location.getLongitude() != 0)
        {
            if (location.getLongitude() < 72.004 || location.getLongitude() > 137.8347 ||
                    location.getLatitude() < 0.8293 || location.getLatitude() > 55.8271)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public TGLocation getLastLocation()
    {
        return lastTGLocation;
    }
}
