package com.lepow.hiremote.lbs.locate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service
{
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	public static final String INTENT_KEY_LOCATION = "location";
	
	private Location currentLocation;
	
	private LocationManager locationManager;
	
	@Override
	public void onCreate()
	{
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		requestGPSLocationUpdates();
		requestNetworkLocationUpdates();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void requestGPSLocationUpdates()
	{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 20, gpsLocationListener);
	}
	
	private void requestNetworkLocationUpdates()
	{
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2 * 1000, 20, networkLocationListener);
	}
	
	private void removeNetworkLocationUpdates()
	{
		locationManager.removeUpdates(networkLocationListener);
	}
	
	private LocationListener gpsLocationListener = new LocationListener()
	{
		private boolean isRemoveNetworkListener = false;
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			if (LocationProvider.OUT_OF_SERVICE == status)
			{
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
			if (isBetterLocation(location, currentLocation))
			{
				updateLocation(location);
			}

			if (location != null && !isRemoveNetworkListener)
			{
				removeNetworkLocationUpdates();
				isRemoveNetworkListener = true;
			}
		}
	};
	
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
			if (isBetterLocation(location, currentLocation))
			{
				updateLocation(location);
			}
		}
	};
	
	//更新位置信息
	private void updateLocation(Location location)
	{
		this.currentLocation = location;
		
		//发广播通知界面处理
		Intent intent = new Intent(this, LocationReciever.class);
		intent.putExtra("INTENT_KEY_LOCATION", location);
		this.sendBroadcast(intent);
	}
	
	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation)
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


}
