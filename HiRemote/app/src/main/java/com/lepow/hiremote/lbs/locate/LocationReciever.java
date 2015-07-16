package com.lepow.hiremote.lbs.locate;

import com.lepow.hiremote.lbs.data.LocationInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

public class LocationReciever extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Location location = getLocation(intent);
		
		LocationInfo locationInfo = new LocationInfo();
		locationInfo.setTime(System.currentTimeMillis());
		locationInfo.setLatitude(location.getLatitude());
		locationInfo.setLongitude(location.getLongitude());
		
		//TODO 地址解析
		
	}
	
	protected Location getLocation(Intent intent)
	{
		return intent.getParcelableExtra(LocationService.INTENT_KEY_LOCATION);
	}
}
