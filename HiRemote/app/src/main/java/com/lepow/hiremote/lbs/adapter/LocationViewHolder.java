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

public class LocationViewHolder extends TGViewHolder<Object>
{
	@FindView(R.id.location_date)
	TextView locationDate;
	
	@FindView(R.id.location_address)
	TextView locationAddress;
	
	@FindView(R.id.location_time)
	TextView locationTime;
	
	@Override
	public View initView(View convertView, ViewGroup parent, int position)
	{
		switch (getAdapter().getItemViewType(position))
		{
			case LocationListAdapter.TYPE_LOCATION:
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_list_item, null);
				
				ButterKnife.bind(this, convertView);
				
				return convertView;
				
			case LocationListAdapter.TYPE_TIME:
				
				TextView textView = new TextView(getContext());
				
				return textView;

			default:
				return null;
		}
	}
	
	@Override
	public void fillData(ViewGroup parent, View convertView, Object itemData, int position)
	{
		switch (getAdapter().getItemViewType(position))
		{
			case LocationListAdapter.TYPE_LOCATION:
				locationDate.setText(((LocationInfo)itemData).getDateString());
				locationTime.setText(((LocationInfo)itemData).getTimeString());
				locationAddress.setText(((LocationInfo)itemData).getAddress());
				break;
				
			case LocationListAdapter.TYPE_TIME:
				((TextView)convertView).setText(((LocationTimeInfo)itemData).getTimeString());
				break;

			default:
				break;
		}
	}

}
