package com.mn.tiger.location;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.receiver.TGHttpResult;

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

        TGHttpLoader<GeoCodeResult> httpLoader = new TGHttpLoader<GeoCodeResult>();
        httpLoader.loadByGet(TGApplication.getInstance(), url, GeoCodeResult.class,
                new TGHttpLoader.OnLoadCallback<GeoCodeResult>()
                {
                    @Override
                    public void onLoadStart()
                    {
                    }

                    @Override
                    public void onLoadSuccess(GeoCodeResult geoCodeResult, TGHttpResult tgHttpResult)
                    {
                        listener.onGeoCodingSuccess(geoCodeResult);
                    }

                    @Override
                    public void onLoadError(int i, String s, TGHttpResult tgHttpResult)
                    {
                        if (null != listener)
                        {
                            listener.onGeoCodingError(i, s);
                        }
                    }

                    @Override
                    public void onLoadCache(GeoCodeResult geoCodeResult, TGHttpResult tgHttpResult)
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
		void onGeoCodingSuccess(GeoCodeResult result);

		/**
		 * 地址解析失败
		 * @param code
		 * @param message
		 */
		void onGeoCodingError(int code, String message);
	}

}
