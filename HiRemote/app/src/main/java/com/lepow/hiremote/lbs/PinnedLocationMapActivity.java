package com.lepow.hiremote.lbs;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.api.AMapManager;
import com.lepow.hiremote.lbs.api.IMapManager;
import com.lepow.hiremote.lbs.data.LocationDataManager;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.IntentKeys;
import com.mn.tiger.widget.TGNavigationBar;

import butterknife.ButterKnife;
import butterknife.FindView;

public class PinnedLocationMapActivity extends BaseActivity
{
	@FindView(R.id.mapview_container)
	LinearLayout mapContainer;
	
	private LocationInfo locationInfo;

	private IMapManager mapManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_map_activity);

		locationInfo = (LocationInfo) getIntent().getSerializableExtra(IntentKeys.LOCATION_INFO);
		
		ButterKnife.bind(this);

		initMapView(savedInstanceState, locationInfo);
	}

	private void initMapView(Bundle savedInstanceState, LocationInfo locationInfo)
	{
		mapManager = new AMapManager(this);
		mapManager.init(mapContainer, savedInstanceState);

		if(null != locationInfo)
		{
			mapManager.addMarker(Double.valueOf(locationInfo.getLatitude()), Double.valueOf(locationInfo.getLongitude()),
					locationInfo.getAddress());
			mapManager.centerTo(Double.valueOf(locationInfo.getLatitude()), Double.valueOf(locationInfo.getLongitude()));
		}
	}

	@Override
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		super.initNavigationResource(navigationBar);
		navigationBar.setBackgroundResource(R.drawable.navi_bar_bg);
	}
}
