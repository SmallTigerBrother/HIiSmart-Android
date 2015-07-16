package com.lepow.hiremote.record.adapter;

import java.util.List;

import android.app.Activity;

import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.lepow.hiremote.record.data.RecordInfo;

public class RecordListAdapter extends TGListAdapter<Object>
{
	static final int TYPE_RECORD = 1;
	
	static final int TYPE_TIME = 2;
	
	public RecordListAdapter(Activity activity, List<Object> items, int convertViewLayoutId,
			Class<? extends TGViewHolder<Object>> viewHolderClass)
	{
		super(activity, items, convertViewLayoutId, viewHolderClass);
	}

	@Override
	public int getItemViewType(int position)
	{
		if(getItem(position) instanceof RecordInfo)
		{
			return TYPE_RECORD;
		}
		else
		{
			return TYPE_TIME;
		}
	}
}
