package com.mn.tiger.location;

import com.lepow.hiremote.R;
import com.mn.tiger.app.TGApplicationProxy;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.error.TGHttpError;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.ToastUtils;

/**
 * Google地址解析功能
 */
public class GoogleGeoCoding
{
	/**
	 * 执行地址解析
	 * @param latitude
	 * @param longitude
	 * @param listener
	 */
	public static void geoCoding(final double latitude, final double longitude,
			final GeoCodeListener listener)
	{
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude
                + "," + longitude + "&sensor=false";

        TGHttpLoader<GoogleGeoCodeResult> httpLoader = new TGHttpLoader<GoogleGeoCodeResult>();
        httpLoader.loadByGet(TGApplicationProxy.getInstance().getApplication(), url, GoogleGeoCodeResult.class,
                new TGHttpLoader.OnLoadCallback<GoogleGeoCodeResult>()
                {
                    @Override
                    public void onLoadStart()
                    {
                    }

                    @Override
                    public void onLoadSuccess(GoogleGeoCodeResult geoCodeResult, TGHttpResult tgHttpResult)
                    {
                        listener.onGeoCodingSuccess(geoCodeResult);
                    }

                    @Override
                    public void onLoadError(int code, String s, TGHttpResult tgHttpResult)
                    {
                        if(code == TGHttpError.IOEXCEPTION || code == TGHttpError.SOCKET_TIMEOUT)
                        {
                            ToastUtils.showToast(TGApplicationProxy.getInstance().getApplication(), R.string.location_permission_deny);
                        }
                        if (null != listener)
                        {
                            listener.onGeoCodingError(code, s);
                        }
                    }

                    @Override
                    public void onLoadCache(GoogleGeoCodeResult geoCodeResult, TGHttpResult tgHttpResult)
                    {
                    }

                    @Override
                    public void onLoadOver()
                    {
                    }
                });
	}

	/**
	 * 地址回调接口
	 */
	public static interface GeoCodeListener
	{
		/**
		 * 地址解析成功
		 * @param result
		 */
		void onGeoCodingSuccess(GoogleGeoCodeResult result);

		/**
		 * 地址解析失败
		 * @param code
		 * @param message
		 */
		void onGeoCodingError(int code, String message);
	}

}
