package com.lepow.hiremote.lbs;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.location.TGLocation;
import com.mn.tiger.location.TGLocationManager;
import com.mn.tiger.log.Logger;
import com.mn.tiger.map.AMapManager;
import com.mn.tiger.map.GoogleMapManager;
import com.mn.tiger.map.IMapManager;
import com.mn.tiger.widget.TGNavigationBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PinnedLocationMapActivity extends BaseActivity
{
	private static final Logger LOG = Logger.getLogger(PinnedLocationMapActivity.class);

	@Bind(R.id.mapview_container)
	FrameLayout mapContainer;
	
	private LocationInfo locationInfo;

	private IMapManager mapManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_map_activity);

		locationInfo = (LocationInfo) getIntent().getSerializableExtra(IntentKeys.LOCATION_INFO);
		
		ButterKnife.bind(this);

		setBarTitleText(getString(R.string.location_history));

		initMapView(savedInstanceState, locationInfo);
	}

	private void initMapView(Bundle savedInstanceState, LocationInfo locationInfo)
	{
		if(TGLocationManager.getInstance().isCurrentLocationInChina())
		{
			LOG.d("[Method:initMapView] use AMapManager");
			mapManager = new AMapManager(this);
		}
		else
		{
			LOG.d("[Method:initMapView] use GoogleMapManager");
			mapManager = new GoogleMapManager(this);
		}

		mapManager.init(mapContainer, savedInstanceState);

		if(null != locationInfo)
		{
			mapManager.addMarker(Double.valueOf(locationInfo.getLatitude()), Double.valueOf(locationInfo.getLongitude()),
					locationInfo.getAddress());
			mapManager.centerTo(Double.valueOf(locationInfo.getLatitude()), Double.valueOf(locationInfo.getLongitude()));
		}
		else
		{
			TGLocation location = TGLocationManager.getInstance().getLastLocation();
			if(null != location)
			{
				mapManager.addMarker(location.getLatitude(), location.getLongitude(),
						location.getAddress());
				mapManager.centerTo(location.getLatitude(), location.getLongitude());
			}
		}
		mapManager.showMyLocation();
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
	}
}
