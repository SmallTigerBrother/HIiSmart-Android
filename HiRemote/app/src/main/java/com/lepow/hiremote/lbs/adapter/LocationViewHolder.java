package com.lepow.hiremote.lbs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.FindView;

import com.mn.tiger.widget.adpter.TGViewHolder;
import com.lepow.hiremote.R;
import com.lepow.hiremote.lbs.data.LocationInfo;
import com.lepow.hiremote.lbs.data.LocationTimeInfo;
import com.mn.tiger.widget.swipelistview.SwipeListViewHolder;

public class LocationViewHolder extends SwipeListViewHolder<LocationInfo>
{
	@FindView(R.id.location_date)
	TextView locationDate;
	
	@FindView(R.id.location_address)
	TextView locationAddress;
	
	@FindView(R.id.location_time)
	TextView locationTime;

	@Override
	protected int getFrontViewId()
	{
		return 0;
	}

	@Override
	protected int getBackViewId()
	{
		return 0;
	}

	@Override
	public void fillData(ViewGroup parent, View convertView, LocationInfo itemData, int position)
	{
		locationDate.setText(((LocationInfo)itemData).getDateString());
		locationTime.setText(((LocationInfo)itemData).getTimeString());
		locationAddress.setText(((LocationInfo) itemData).getAddress());
	}

}
