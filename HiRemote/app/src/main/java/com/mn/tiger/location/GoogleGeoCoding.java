package com.mn.tiger.location;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

/**
 * Google地址解析功能
 */
public class GoogleGeoCoding
{
	/**
	 * 执行地址解析
	 * @param latitude
	 * @param longatude
	 * @param listener
	 */
	public static void geoCoding(final double latitude, final double longatude,
			final GeoCodeListener listener)
	{
		Executors.newCachedThreadPool().execute(new Runnable()
		{
			@Override
			public void run()
			{
				String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude
						+ "," + longatude + "&sensor=false";
				// 创建一个HttpClient对象
				HttpClient httpClient = new DefaultHttpClient();
				String responseData = "";
				try
				{
					// 向指定的URL发送Http请求
					HttpResponse response = httpClient.execute(new HttpGet(url));
					// 取得服务器返回的响应
					HttpEntity entity = response.getEntity();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity
							.getContent()));
					String line = "";
					while ((line = bufferedReader.readLine()) != null)
					{
						responseData = responseData + line;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					if (null != listener)
					{
						listener.onGeoCodingError(0, e.getMessage());
					}
				}

				if (null != listener)
				{
					listener.onGeoCodingSuccess(new Gson().fromJson(responseData,
							GeoCodeResult.class));
				}
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
