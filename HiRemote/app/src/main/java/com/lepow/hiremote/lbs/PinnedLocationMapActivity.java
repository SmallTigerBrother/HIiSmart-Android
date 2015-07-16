package com.lepow.hiremote.lbs;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.FindView;

import com.google.android.gms.maps.MapView;
import com.lepow.hiremote.R;
import com.lepow.hiremote.app.BaseActivity;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.misc.IntentKeys;

public class PinnedLocationMapActivity extends BaseActivity
{
	@FindView(R.id.mapview)
	MapView mapView;
	
	private LocationInfo locationInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinned_location_map_activity);
		
		locationInfo = (LocationInfo) getIntent().getSerializableExtra(IntentKeys.LOCATION_INFO);
		
		ButterKnife.bind(this);
	}
}
