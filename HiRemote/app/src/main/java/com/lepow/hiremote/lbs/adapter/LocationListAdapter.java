package com.lepow.hiremote.lbs.adapter;

import java.util.List;

import android.app.Activity;

import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.lepow.hiremote.lbs.data.LocationInfo;

public class LocationListAdapter extends TGListAdapter<Object>
{
	static final int TYPE_LOCATION = 1;
	
	static final int TYPE_TIME = 2;
	
	public LocationListAdapter(Activity activity, List<Object> items, int convertViewLayoutId,
			Class<? extends TGViewHolder<Object>> viewHolderClass)
	{
		super(activity, items, convertViewLayoutId, viewHolderClass);
	}

	@Override
	public int getItemViewType(int position)
	{
		if(getItem(position) instanceof LocationInfo)
		{
			return TYPE_LOCATION;
		}
		else
		{
			return TYPE_TIME;
		}
	}
}
